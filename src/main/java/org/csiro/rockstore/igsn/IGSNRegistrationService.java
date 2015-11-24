package org.csiro.rockstore.igsn;

import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.csiro.rockstore.entity.postgres.RsSample;
import org.csiro.rockstore.entity.postgres.RsSubcollection;
import org.csiro.rockstore.entity.service.SampleEntityService;
import org.csiro.rockstore.entity.service.SubCollectionEntityService;
import org.csiro.rockstore.http.HttpServiceProvider;
import org.csiro.rockstore.igsn.bindings.IdentifierType;
import org.csiro.rockstore.igsn.bindings.ObjectFactory;
import org.csiro.rockstore.igsn.bindings.Samples;
import org.csiro.rockstore.igsn.bindings.Samples.Sample;
import org.csiro.rockstore.igsn.bindings.Samples.Sample.MaterialTypes;
import org.csiro.rockstore.igsn.bindings.Samples.Sample.SampleCollectors;
import org.csiro.rockstore.igsn.bindings.Samples.Sample.SampleCollectors.Collector;
import org.csiro.rockstore.igsn.bindings.Samples.Sample.SampleCuration.Curation;
import org.csiro.rockstore.igsn.bindings.Samples.Sample.SampleTypes;
import org.csiro.rockstore.igsn.bindings.Samples.Sample.SamplingLocation;
import org.csiro.rockstore.igsn.bindings.Samples.Sample.SamplingMethod;
import org.csiro.rockstore.igsn.bindings.Samples.Sample.SamplingTime;
import org.csiro.rockstore.utilities.Config;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class IGSNRegistrationService{

	private final Log log = LogFactory.getLog(getClass());
	
	private ObjectFactory objectFactory;
	
	private static IGSNRegistrationService service;
	
	private static boolean isRunning;
	
	private HttpServiceProvider httpServiceProvider;
	
	SubCollectionEntityService  subcollectionService;
	SampleEntityService sampleEntityService;
	
	public IGSNRegistrationService(HttpServiceProvider httpServiceProvider){
		this.httpServiceProvider = httpServiceProvider;
		IGSNRegistrationService.service=null;
		isRunning=false;
		this.objectFactory=new ObjectFactory();
		subcollectionService = new SubCollectionEntityService();
		sampleEntityService = new SampleEntityService();
	}
	
	
	public  synchronized void run(){
		setIsrunning(true);
		try{
			this.registerSamples();
			
		}catch(Exception e){
			e.printStackTrace();
		}
				
		try{
			this.registerSubCollections();			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			setIsrunning(false);
		}
	}
	
	public void mint(Samples samplesXML) throws Exception{
		HttpPost post= new HttpPost(Config.getIGSNUrl()+"igsn/mint");
		
		StringWriter writer = new StringWriter();
	    JAXBContext jaxbContext = JAXBContext.newInstance(Samples.class);
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	    jaxbMarshaller.marshal(samplesXML, writer);
	    
	    post.addHeader("content-type", "application/xml");
        
        //Set the request post body
        StringEntity userEntity = new StringEntity(writer.getBuffer().toString());
        post.setEntity(userEntity);
        
        HttpResponse response = httpServiceProvider.invokeTheMethod(post);
        
     
        JsonElement ele = new JsonParser().parse(IOUtils.toString(response.getEntity().getContent()));
        
        Set<Map.Entry<String,JsonElement>> entries = ele.getAsJsonObject().entrySet();
        for(Map.Entry<String,JsonElement> entry:entries) {
           System.out.println(entry.getKey());  //get keys
           System.out.println(entry.getValue());  //get keys
        } 
        
	}
	
	public synchronized Samples registerSubCollections() throws FileNotFoundException{
		List<RsSubcollection> samples=subcollectionService.getUnminted();
		
		Samples samplesXML = new Samples();
		
		for(RsSubcollection o:samples){
			samplesXML.getSample().add(this.register(o));
		}
		
		return samplesXML;
	}
	
	public synchronized Samples registerSamples() throws FileNotFoundException{
		
		List<RsSample> samples=sampleEntityService.getUnminted();
		
		Samples samplesXML = new Samples();
		
		for(RsSample o:samples){
			samplesXML.getSample().add(this.register(o));
		}
		
		return samplesXML;
	}
	
	public synchronized Sample register(RsSubcollection rsc) throws FileNotFoundException{
		Samples.Sample sampleXml = new Samples.Sample();
		Samples.Sample.SampleNumber sampleNumberXml = new Samples.Sample.SampleNumber();
		
		sampleNumberXml.setIdentifierType(IdentifierType.fromValue("igsn"));
		sampleNumberXml.setValue(rsc.getIgsn());
		
		sampleXml.setSampleName(rsc.getSubcollectionId());
		sampleXml.setSampleNumber(sampleNumberXml);
		
		Samples.Sample.IsPublic isPublic = new Samples.Sample.IsPublic();
		isPublic.setValue(true);
		sampleXml.setIsPublic(isPublic);
		sampleXml.setLandingPage(Config.getIGSNLanding()+"browsesubcollections/" + rsc.getIgsn());	
		
		Samples.Sample.SampleTypes sampleTypesXml = new Samples.Sample.SampleTypes();
		JAXBElement<SampleTypes> sampleTypeJAXBElement = objectFactory.createSamplesSampleSampleTypes(sampleTypesXml);					
		sampleTypesXml.setNilReason("http://www.opengis.net/def/nil/OGC/0/unknown");				
		sampleTypesXml.getSampleType().add(null);
		sampleTypeJAXBElement.setNil(true);							
		sampleXml.setSampleTypes(sampleTypeJAXBElement);
		
		Samples.Sample.MaterialTypes materialType = new Samples.Sample.MaterialTypes();
		JAXBElement<MaterialTypes> materialTypeJAXBElement = this.objectFactory.createSamplesSampleMaterialTypes(materialType);							
		materialType.setNilReason("http://www.opengis.net/def/nil/OGC/0/unknown");
		materialTypeJAXBElement.setNil(true);										
		sampleXml.setMaterialTypes(materialTypeJAXBElement);
		
		Samples.Sample.Classification classification = new Samples.Sample.Classification();
		classification.setClassificationIdentifier("UNKNOWN");
		classification.setValue("UNKNOWN");
		sampleXml.setClassification(classification);
				
		Samples.Sample.SamplingLocation samplingLocation = new Samples.Sample.SamplingLocation();	
		JAXBElement<SamplingLocation> samplingLocationJAXBElement = this.objectFactory.createSamplesSampleSamplingLocation(samplingLocation);
		samplingLocation.setNilReason("http://www.opengis.net/def/nil/OGC/0/unknown");
		samplingLocationJAXBElement.setNil(true);							
		sampleXml.setSamplingLocation(samplingLocationJAXBElement);
		
		Samples.Sample.SamplingTime samplingTime = new Samples.Sample.SamplingTime();	
		samplingTime.setNilReason("http://www.opengis.net/def/nil/OGC/0/unknown");
		JAXBElement<SamplingTime> samplingTimeJAXBElement = this.objectFactory.createSamplesSampleSamplingTime(samplingTime);
		samplingTimeJAXBElement.setNil(true);
		sampleXml.setSamplingTime(samplingTimeJAXBElement);		
		
		Samples.Sample.SampleCollectors sampleCollectors = new Samples.Sample.SampleCollectors();
		JAXBElement<SampleCollectors> sampleCollectorJAXBElement = this.objectFactory.createSamplesSampleSampleCollectors(sampleCollectors);
		String staffIdManager = rsc.getRsCollection().getStaffIdFieldManager();
		if(staffIdManager.isEmpty()){
			sampleCollectors.setNilReason("http://www.opengis.net/def/nil/OGC/0/unknown");
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
		samplingMethod.setNilReason("http://www.opengis.net/def/nil/OGC/0/unknown");
		samplingMethodJAXBElement.setNil(true);			
		sampleXml.setSamplingMethod(samplingMethodJAXBElement);//VT: TODO- check null
		
		
		Samples.Sample.SampleCuration sampleCurationXml = new Samples.Sample.SampleCuration();		
		Curation c= new Curation();		
		c.setCurator("CSIRO");
		
		//VT Set curator
		sampleCurationXml.getCuration().add(c);					
		sampleXml.setSampleCuration(sampleCurationXml);	
		
		return sampleXml;	
	}
	
	public synchronized Sample register(RsSample rsc) throws FileNotFoundException{
		Samples.Sample sampleXml = new Samples.Sample();
		Samples.Sample.SampleNumber sampleNumberXml = new Samples.Sample.SampleNumber();
		
		sampleNumberXml.setIdentifierType(IdentifierType.fromValue("igsn"));
		sampleNumberXml.setValue(rsc.getIgsn());
		
		sampleXml.setSampleName(rsc.getCsiroSampleId());
		sampleXml.setSampleNumber(sampleNumberXml);
		
		Samples.Sample.IsPublic isPublic = new Samples.Sample.IsPublic();
		isPublic.setValue(true);
		sampleXml.setIsPublic(isPublic);
		sampleXml.setLandingPage(Config.getIGSNLanding()+"browsesamples/" + rsc.getIgsn());	
		
		Samples.Sample.SampleTypes sampleTypesXml = new Samples.Sample.SampleTypes();
		JAXBElement<SampleTypes> sampleTypeJAXBElement = objectFactory.createSamplesSampleSampleTypes(sampleTypesXml);					
		sampleTypesXml.setNilReason("http://www.opengis.net/def/nil/OGC/0/unknown");				
		sampleTypesXml.getSampleType().add(null);
		sampleTypeJAXBElement.setNil(true);							
		sampleXml.setSampleTypes(sampleTypeJAXBElement);
		
		Samples.Sample.MaterialTypes materialType = new Samples.Sample.MaterialTypes();
		JAXBElement<MaterialTypes> materialTypeJAXBElement = this.objectFactory.createSamplesSampleMaterialTypes(materialType);							
		materialType.setNilReason("http://www.opengis.net/def/nil/OGC/0/unknown");
		materialTypeJAXBElement.setNil(true);										
		sampleXml.setMaterialTypes(materialTypeJAXBElement);
		
		Samples.Sample.Classification classification = new Samples.Sample.Classification();
		classification.setClassificationIdentifier("UNKNOWN");
		classification.setValue("UNKNOWN");
		sampleXml.setClassification(classification);
				
		Samples.Sample.SamplingLocation samplingLocation = new Samples.Sample.SamplingLocation();	
		JAXBElement<SamplingLocation> samplingLocationJAXBElement = this.objectFactory.createSamplesSampleSamplingLocation(samplingLocation);
		samplingLocation.setNilReason("http://www.opengis.net/def/nil/OGC/0/unknown");
		samplingLocationJAXBElement.setNil(true);							
		sampleXml.setSamplingLocation(samplingLocationJAXBElement);
		
		Samples.Sample.SamplingTime samplingTime = new Samples.Sample.SamplingTime();	
		samplingTime.setNilReason("http://www.opengis.net/def/nil/OGC/0/unknown");
		JAXBElement<SamplingTime> samplingTimeJAXBElement = this.objectFactory.createSamplesSampleSamplingTime(samplingTime);
		samplingTimeJAXBElement.setNil(true);
		sampleXml.setSamplingTime(samplingTimeJAXBElement);		
		
		Samples.Sample.SampleCollectors sampleCollectors = new Samples.Sample.SampleCollectors();
		JAXBElement<SampleCollectors> sampleCollectorJAXBElement = this.objectFactory.createSamplesSampleSampleCollectors(sampleCollectors);
		String sampleCollector = rsc.getSampleCollector();
		if(sampleCollector.isEmpty()){
			sampleCollectors.setNilReason("http://www.opengis.net/def/nil/OGC/0/unknown");
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
		samplingMethod.setNilReason("http://www.opengis.net/def/nil/OGC/0/unknown");
		samplingMethodJAXBElement.setNil(true);			
		sampleXml.setSamplingMethod(samplingMethodJAXBElement);//VT: TODO- check null
		
		
		Samples.Sample.SampleCuration sampleCurationXml = new Samples.Sample.SampleCuration();		
		Curation c= new Curation();		
		c.setCurator("CSIRO");
		
		//VT Set curator
		sampleCurationXml.getCuration().add(c);					
		sampleXml.setSampleCuration(sampleCurationXml);	
		
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
