package org.csiro.rockstore.igsn;

import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.csiro.igsn.bindings.allocation2_0.EventType;
import org.csiro.igsn.bindings.allocation2_0.IdentifierType;
import org.csiro.igsn.bindings.allocation2_0.NilReasonType;
import org.csiro.igsn.bindings.allocation2_0.ObjectFactory;
import org.csiro.igsn.bindings.allocation2_0.Samples;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.MaterialTypes;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SampleCollectors;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SampleCollectors.Collector;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SampleCuration.Curation;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SampleTypes;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SamplingLocation;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SamplingMethod;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SamplingTime;
import org.csiro.igsn.bindings.allocation2_0.SpatialType;
import org.csiro.rockstore.entity.postgres.IGSNLog;
import org.csiro.rockstore.entity.postgres.RsSample;
import org.csiro.rockstore.entity.postgres.RsSubcollection;
import org.csiro.rockstore.entity.service.IGSNEntityService;
import org.csiro.rockstore.entity.service.SampleEntityService;
import org.csiro.rockstore.entity.service.SubCollectionEntityService;
import org.csiro.rockstore.http.HttpServiceProvider;
import org.csiro.rockstore.utilities.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class IGSNRegistrationService{

	private final Log log = LogFactory.getLog(getClass());
	
	private ObjectFactory objectFactory;
	
	private static IGSNRegistrationService service;
	
	private static boolean isRunning;
	
	private HttpServiceProvider httpServiceProvider;
	
	SubCollectionEntityService  subcollectionService;
	SampleEntityService sampleEntityService;
	IGSNEntityService igsnEntityService;
	
	@Autowired
	public IGSNRegistrationService(HttpServiceProvider httpServiceProvider){
		this.httpServiceProvider = httpServiceProvider;
		IGSNRegistrationService.service=null;
		isRunning=false;
		this.objectFactory=new ObjectFactory();
		subcollectionService = new SubCollectionEntityService();
		sampleEntityService = new SampleEntityService();
		igsnEntityService = new IGSNEntityService();
	}
	
	
	public  synchronized void run(){
		setIsrunning(true);
		try{
			mint(this.registerSamples());						
		}catch(Exception e){
			e.printStackTrace();
		}
				
		try{
			mint(this.registerSubCollections());			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			setIsrunning(false);
		}
	}
	
	public void test() throws Exception{
		HttpGet get = new HttpGet("http://localhost:8080/CSIRO-IGSN/subnamespace/list/all");
		HttpResponse response = httpServiceProvider.invokeTheMethod(get);
		System.out.print(IOUtils.toString(response.getEntity().getContent()));
	}
	
	public void mint(Samples samplesXML) throws Exception{
		if(samplesXML==null){
			return;
		}
		HttpPost post= new HttpPost(Config.getIGSNUrl()+"igsn/mint");
		
		StringWriter writer = new StringWriter();
	    JAXBContext jaxbContext = JAXBContext.newInstance(Samples.class);
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	    jaxbMarshaller.marshal(samplesXML, writer);
	    
	    post.addHeader("content-type", "application/xml");
        
        //Set the request post body
	    String postBody = writer.getBuffer().toString();
        StringEntity userEntity = new StringEntity(postBody);
        post.setEntity(userEntity);
        
        System.out.println(postBody);
        
        HttpResponse response = httpServiceProvider.invokeTheMethod(post);
        
     
        JsonElement ele = new JsonParser().parse(IOUtils.toString(response.getEntity().getContent()));
        
        JsonArray entries = ele.getAsJsonArray();
        for(JsonElement entry:entries) {
        	JsonObject jobj = entry.getAsJsonObject();
        	if(jobj.get("mintStatusCode").getAsInt()==200 && jobj.get("databaseStatusCode").getAsInt()==200){
        		IGSNLog ml= new IGSNLog(jobj.get("sampleId").getAsString(),jobj.get("handle").getAsString());
        		igsnEntityService.persist(ml);
        	}
        } 
        
	}
	
	public  synchronized Samples registerSubCollections() throws FileNotFoundException{
		List<RsSubcollection> samples=subcollectionService.getUnminted();
		if(samples.size() ==0 ){
			return null;
		}
		Samples samplesXML = new Samples();
		
		for(RsSubcollection o:samples){
			samplesXML.getSample().add(this.register(o));
		}
		
		return samplesXML;
	}
	
	public synchronized Samples registerSamples() throws FileNotFoundException{
		
		List<RsSample> samples=sampleEntityService.getUnminted();
		if(samples.size() ==0 ){
			return null;
		}
		Samples samplesXML = new Samples();
		
		for(RsSample o:samples){
			samplesXML.getSample().add(this.register(o));
		}
		
		return samplesXML;
	}
	
	public  Sample register(RsSubcollection rsc) throws FileNotFoundException{
		Samples.Sample sampleXml = new Samples.Sample();
		Samples.Sample.SampleNumber sampleNumberXml = new Samples.Sample.SampleNumber();
		
		sampleNumberXml.setIdentifierType(IdentifierType.fromValue("igsn"));
		sampleNumberXml.setValue(rsc.getIgsn());
		
		sampleXml.setSampleName(rsc.getRsCollection().getProject() + ":"+ rsc.getSubcollectionId());
		sampleXml.setSampleNumber(sampleNumberXml);
		
		Samples.Sample.IsPublic isPublic = new Samples.Sample.IsPublic();
		isPublic.setValue(true);
		sampleXml.setIsPublic(isPublic);
		sampleXml.setLandingPage(Config.getIGSNLanding()+"browsesubcollections/" + rsc.getIgsn());	
		
		Samples.Sample.SampleTypes sampleTypesXml = new Samples.Sample.SampleTypes();
		JAXBElement<SampleTypes> sampleTypeJAXBElement = objectFactory.createSamplesSampleSampleTypes(sampleTypesXml);					
		sampleTypesXml.setNilReason(NilReasonType.UNKNOWN.value());				
		sampleTypesXml.getSampleType().add(null);
		sampleTypeJAXBElement.setNil(true);							
		sampleXml.setSampleTypes(sampleTypeJAXBElement);
		
		Samples.Sample.MaterialTypes materialType = new Samples.Sample.MaterialTypes();
		JAXBElement<MaterialTypes> materialTypeJAXBElement = this.objectFactory.createSamplesSampleMaterialTypes(materialType);							
		materialType.getMaterialType().add("http://vocabulary.odm2.org/medium/rock");
		sampleXml.setMaterialTypes(materialTypeJAXBElement);
		
		Samples.Sample.Classification classification = new Samples.Sample.Classification();
		classification.setClassificationIdentifier(NilReasonType.UNKNOWN.value());
		classification.setValue(NilReasonType.UNKNOWN.value());
		sampleXml.setClassification(classification);
		
		sampleXml.setPurpose("Rock subcollection from rockstore");
				
		Samples.Sample.SamplingLocation samplingLocation = new Samples.Sample.SamplingLocation();	
		JAXBElement<SamplingLocation> samplingLocationJAXBElement = this.objectFactory.createSamplesSampleSamplingLocation(samplingLocation);
		samplingLocation.setNilReason(NilReasonType.UNKNOWN.value());
		samplingLocationJAXBElement.setNil(true);							
		sampleXml.setSamplingLocation(samplingLocationJAXBElement);
		
		Samples.Sample.SamplingTime samplingTime = new Samples.Sample.SamplingTime();	
		samplingTime.setNilReason(NilReasonType.UNKNOWN.value());
		JAXBElement<SamplingTime> samplingTimeJAXBElement = this.objectFactory.createSamplesSampleSamplingTime(samplingTime);
		samplingTimeJAXBElement.setNil(true);
		sampleXml.setSamplingTime(samplingTimeJAXBElement);		
		
		Samples.Sample.SampleCollectors sampleCollectors = new Samples.Sample.SampleCollectors();
		JAXBElement<SampleCollectors> sampleCollectorJAXBElement = this.objectFactory.createSamplesSampleSampleCollectors(sampleCollectors);
		String staffIdManager = rsc.getRsCollection().getStaffIdFieldManager();
		if(staffIdManager.isEmpty()){
			sampleCollectors.setNilReason(NilReasonType.UNKNOWN.value());
			sampleCollectorJAXBElement.setNil(true);				
		}else{
			Collector collector = new Collector();
			collector.setCollectorIdentifier(staffIdManager);
			collector.setValue(staffIdManager);
			sampleCollectors.getCollector().add(collector);
		}
		sampleXml.setSampleCollectors(sampleCollectorJAXBElement);
		
		
		Samples.Sample.SamplingMethod samplingMethod= new Samples.Sample.SamplingMethod();
		JAXBElement<SamplingMethod> samplingMethodJAXBElement = this.objectFactory.createSamplesSampleSamplingMethod(samplingMethod);		
		samplingMethod.setNilReason(NilReasonType.UNKNOWN.value());
		samplingMethodJAXBElement.setNil(true);			
		sampleXml.setSamplingMethod(samplingMethodJAXBElement);//VT: TODO- check null
		
		
		Samples.Sample.SampleCuration sampleCurationXml = new Samples.Sample.SampleCuration();		
		Curation c= new Curation();		
		c.setCurator("CSIRO");
		
		//VT Set curator
		sampleCurationXml.getCuration().add(c);					
		sampleXml.setSampleCuration(sampleCurationXml);	
		
		Calendar cal = Calendar.getInstance();
		Samples.Sample.LogElement logElement = new Samples.Sample.LogElement();
		logElement.setValue("rockstore: SubCollection");		
		cal.setTime(new Date());				  
		logElement.setTimeStamp(String.valueOf(cal.get(Calendar.YEAR)));					
		logElement.setEvent(EventType.SUBMITTED);		
		sampleXml.setLogElement(logElement);
		
		return sampleXml;	
	}
	
	public  Sample register(RsSample rsc) throws FileNotFoundException{
		Samples.Sample sampleXml = new Samples.Sample();
		Samples.Sample.SampleNumber sampleNumberXml = new Samples.Sample.SampleNumber();
		
		sampleNumberXml.setIdentifierType(IdentifierType.fromValue("igsn"));
		sampleNumberXml.setValue(rsc.getIgsn());
		
		sampleXml.setSampleName(rsc.getRsSubcollection().getRsCollection().getProject() + ":" + (rsc.getCsiroSampleId()==null || rsc.getCsiroSampleId().isEmpty()?rsc.getIgsn():rsc.getCsiroSampleId()));
		sampleXml.setSampleNumber(sampleNumberXml);
		
		Samples.Sample.IsPublic isPublic = new Samples.Sample.IsPublic();
		isPublic.setValue(true);
		sampleXml.setIsPublic(isPublic);
		sampleXml.setLandingPage(Config.getIGSNLanding()+"browsesamples/" + rsc.getIgsn());	
		
		Samples.Sample.SampleTypes sampleTypesXml = new Samples.Sample.SampleTypes();
		JAXBElement<SampleTypes> sampleTypeJAXBElement = objectFactory.createSamplesSampleSampleTypes(sampleTypesXml);					
		sampleTypesXml.setNilReason(NilReasonType.UNKNOWN.value());				
		sampleTypesXml.getSampleType().add(null);
		sampleTypeJAXBElement.setNil(true);							
		sampleXml.setSampleTypes(sampleTypeJAXBElement);
		
		Samples.Sample.MaterialTypes materialType = new Samples.Sample.MaterialTypes();
		JAXBElement<MaterialTypes> materialTypeJAXBElement = this.objectFactory.createSamplesSampleMaterialTypes(materialType);							
		materialType.getMaterialType().add("http://vocabulary.odm2.org/medium/rock");
		sampleXml.setMaterialTypes(materialTypeJAXBElement);
		
		Samples.Sample.Classification classification = new Samples.Sample.Classification();
		if(rsc.getSampleType()==null || rsc.getSampleType().isEmpty()){
			classification.setClassificationIdentifier(NilReasonType.UNKNOWN.value());
			classification.setValue("UNKNOWN");
		}else{
			classification.setClassificationIdentifier(NilReasonType.UNKNOWN.value());
			classification.setValue(rsc.getSampleType());
		}
		sampleXml.setClassification(classification);
		
		sampleXml.setPurpose("Rock sample from rockstore");
				
		Samples.Sample.SamplingLocation samplingLocation = new Samples.Sample.SamplingLocation();	
		JAXBElement<SamplingLocation> samplingLocationJAXBElement = this.objectFactory.createSamplesSampleSamplingLocation(samplingLocation);
		
		if(rsc.getLocation()!=null){			
			//VT: sample elevation
			Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureLocation.Wkt wkt = new Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureLocation.Wkt();
			wkt.setSrs("EPSG:4326");
			wkt.setSpatialType(SpatialType.POINT);
			wkt.setValue(rsc.getLat() + " " + rsc.getLon());
			samplingLocation.setWkt(wkt);
		}else{
			samplingLocation.setNilReason(NilReasonType.MISSING.value());
			samplingLocationJAXBElement.setNil(true);
		}						
		sampleXml.setSamplingLocation(samplingLocationJAXBElement);
		
		Samples.Sample.SamplingTime samplingTime = new Samples.Sample.SamplingTime();	
		samplingTime.setNilReason(NilReasonType.UNKNOWN.value());
		JAXBElement<SamplingTime> samplingTimeJAXBElement = this.objectFactory.createSamplesSampleSamplingTime(samplingTime);
		samplingTimeJAXBElement.setNil(true);
		sampleXml.setSamplingTime(samplingTimeJAXBElement);		
		
		Samples.Sample.SampleCollectors sampleCollectors = new Samples.Sample.SampleCollectors();
		JAXBElement<SampleCollectors> sampleCollectorJAXBElement = this.objectFactory.createSamplesSampleSampleCollectors(sampleCollectors);
		String sampleCollector = rsc.getSampleCollector();
		if(sampleCollector==null || sampleCollector.isEmpty()){
			sampleCollectors.setNilReason(NilReasonType.UNKNOWN.value());
			sampleCollectorJAXBElement.setNil(true);				
		}else{
			Collector collector = new Collector();
			collector.setCollectorIdentifier(sampleCollector);
			collector.setValue(sampleCollector);
			sampleCollectors.getCollector().add(collector);
		}
		sampleXml.setSampleCollectors(sampleCollectorJAXBElement);
		
		
		Samples.Sample.SamplingMethod samplingMethod= new Samples.Sample.SamplingMethod();
		JAXBElement<SamplingMethod> samplingMethodJAXBElement = this.objectFactory.createSamplesSampleSamplingMethod(samplingMethod);		
		samplingMethod.setNilReason(NilReasonType.UNKNOWN.value());
		samplingMethodJAXBElement.setNil(true);			
		sampleXml.setSamplingMethod(samplingMethodJAXBElement);//VT: TODO- check null
		
		
		Samples.Sample.SampleCuration sampleCurationXml = new Samples.Sample.SampleCuration();		
		Curation c= new Curation();		
		c.setCurator("CSIRO");
		
		//VT Set curator
		sampleCurationXml.getCuration().add(c);					
		sampleXml.setSampleCuration(sampleCurationXml);	
		
		Calendar cal = Calendar.getInstance();
		Samples.Sample.LogElement logElement = new Samples.Sample.LogElement();
		logElement.setValue("rockstore: Samples");		
		cal.setTime(new Date());				  
		logElement.setTimeStamp(String.valueOf(cal.get(Calendar.YEAR)));					
		logElement.setEvent(EventType.SUBMITTED);		
		sampleXml.setLogElement(logElement);
		
		return sampleXml;	
	}
	
	
	public static synchronized void setIsrunning(boolean running){
		isRunning=running;
	}
	
	

	public void setHttpServiceProvider(HttpServiceProvider httpServiceProvider) {
		this.httpServiceProvider = httpServiceProvider;
	}


	public static IGSNRegistrationService getInstance() {
		if(IGSNRegistrationService.service==null){
			IGSNRegistrationService.service = new IGSNRegistrationService(new HttpServiceProvider());
		}
		return IGSNRegistrationService.service;
	}


	public  boolean isRunning() {
		return isRunning;
	}


}
