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
import org.csiro.igsn.jaxb.registration.bindings.EventType;
import org.csiro.igsn.jaxb.registration.bindings.ObjectFactory;
import org.csiro.igsn.jaxb.registration.bindings.Resources;
import org.csiro.igsn.jaxb.registration.bindings.Resources.Resource;
import org.csiro.igsn.jaxb.registration.bindings.Resources.Resource.Classifications.Classification;
import org.csiro.igsn.jaxb.registration.bindings.Resources.Resource.Contributors.Contributor;
import org.csiro.igsn.jaxb.registration.bindings.Resources.Resource.CurationDetails;
import org.csiro.igsn.jaxb.registration.bindings.Resources.Resource.CurationDetails.Curation;
import org.csiro.igsn.jaxb.registration.bindings.Resources.Resource.Location;
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
	
	
	
	public void mint(Resources resourcesXML) throws Exception{
		if(resourcesXML==null){
			return;
		}
		HttpPost post= new HttpPost(Config.getIGSNUrl()+"api/igsn/30/mint");
		
		StringWriter writer = new StringWriter();
	    JAXBContext jaxbContext = JAXBContext.newInstance(Resources.class);
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	    jaxbMarshaller.marshal(resourcesXML, writer);
	    
	    post.addHeader("content-type", "application/xml");
        
        //Set the request post body
	    String postBody = writer.getBuffer().toString();
        StringEntity userEntity = new StringEntity(postBody);
        post.setEntity(userEntity);               
        
        HttpResponse response = httpServiceProvider.invokeTheMethod(post);        
     
        JsonElement ele = new JsonParser().parse(IOUtils.toString(response.getEntity().getContent()));
        
        JsonArray entries = ele.getAsJsonArray();
        for(JsonElement entry:entries) {
        	JsonObject jobj = entry.getAsJsonObject();
        	if(jobj.get("mintStatusCode").getAsInt()==200 && jobj.get("databaseStatusCode").getAsInt()==200){
        		IGSNLog ml= new IGSNLog(jobj.get("sampleId").getAsString(),jobj.get("handle").getAsString());
        		igsnEntityService.persist(ml);
        	}else if(jobj.get("mintStatusCode").getAsInt()==200 && jobj.get("databaseStatusCode").getAsInt()==104){
        		//VT: For some reason perhaps deal to network failure whilst minting, if a igsn already exist, we will attempt to update instead
        		//VT: we will first try sample and if not found, we can safely assume it is in subcollection        		
        		RsSample rs = this.sampleEntityService.searchByIGSN(jobj.get("sampleId").getAsString());
        		if(rs!=null){  
        			try{        				
    		       		Resources sampleXMLRetry = new Resources();		    				    		
    		       		sampleXMLRetry.getResource().add(this.register(rs,true));
    		       		this.mint(sampleXMLRetry);
    		       	}catch(Exception e){
    		       		log.error(e);
    		       	}
        			
        		}else {   //VT: rsSample is null therefore it has to be subcollection     			
        			try{
        				RsSubcollection rsc = this.subcollectionService.searchByIGSN(jobj.get("sampleId").getAsString());
    		       		Resources subCollectionXMLRetry = new Resources();		    				    		
    		       		subCollectionXMLRetry.getResource().add(this.register(rsc,true));
    		       		this.mint(subCollectionXMLRetry);
    		       	}catch(Exception e){
    		       		log.error(e);
    		       	}
        		}	    		        		
        		        	
        	}
        } 
        
	}
	
	public  synchronized Resources registerSubCollections() throws FileNotFoundException{
		List<RsSubcollection> samples=subcollectionService.getUnminted();
		if(samples.size() ==0 ){
			return null;
		}
		Resources resourceXML = new Resources();
		
		for(RsSubcollection o:samples){
			resourceXML.getResource().add(this.register(o,false));
		}
		
		return resourceXML;
	}
	
	public synchronized Resources registerSamples() throws FileNotFoundException{
		
		List<RsSample> samples=sampleEntityService.getUnminted();
		if(samples.size() ==0 ){
			return null;
		}
		Resources resourcesXML = new Resources();
		
		for(RsSample o:samples){
			resourcesXML.getResource().add(this.register(o,false));
		}
		
		return resourcesXML;
	}
	
	public  Resource register(RsSubcollection rsc,boolean update) throws FileNotFoundException{
		Resource resourceXML = this.objectFactory.createResourcesResource();
		resourceXML.setResourceIdentifier(this.objectFactory.createResourcesResourceResourceIdentifier());
		resourceXML.getResourceIdentifier().setValue(rsc.getIgsn());
		resourceXML.setResourceTitle(rsc.getRsCollection().getProject() + ":"+ rsc.getSubcollectionId());
		
		resourceXML.setRegisteredObjectType("http://pid.geoscience.gov.au/def/voc/igsn-codelists/PhysicalSample");
	
		resourceXML.setIsPublic(this.objectFactory.createResourcesResourceIsPublic());
		resourceXML.getIsPublic().setValue(true);
		
		resourceXML.setLandingPage(Config.getIGSNLanding()+"browsesubcollections/" + rsc.getIgsn());
		
		
		resourceXML.setResourceTypes(this.objectFactory.createResourcesResourceResourceTypes());
		resourceXML.getResourceTypes().getResourceType().add("http://www.opengis.net/def/nil/OGC/0/unknown");
		
		resourceXML.setMaterialTypes(this.objectFactory.createResourcesResourceMaterialTypes());
		resourceXML.getMaterialTypes().getMaterialType().add("http://pid.geoscience.gov.au/def/voc/igsn-codelists/rock");
		
		resourceXML.setPurpose("Rock subcollection from rockstore");
				
		
		
		String staffIdManager = rsc.getRsCollection().getStaffIdFieldManager();
		if(!staffIdManager.isEmpty()){
			resourceXML.setContributors(this.objectFactory.createResourcesResourceContributors());			
			Contributor contributorXML = this.objectFactory.createResourcesResourceContributorsContributor();
			contributorXML.setContributorType("http://registry.it.csiro.au/def/isotc211/CI_RoleCode/originator");
			contributorXML.setContributorName(staffIdManager);
			resourceXML.getContributors().getContributor().add(contributorXML);
		}
			
		resourceXML.setCurationDetails(this.objectFactory.createResourcesResourceCurationDetails());
		
		Curation curationXML = this.objectFactory.createResourcesResourceCurationDetailsCuration();
		curationXML.setCurator("CSIRO Rockstore");
		curationXML.setCuratingInstitution(this.objectFactory.createResourcesResourceCurationDetailsCurationCuratingInstitution());
		curationXML.getCuratingInstitution().setInstitutionURI("http://csiro.au");
		curationXML.getCuratingInstitution().setValue("CSIRO");
		resourceXML.getCurationDetails().getCuration().add(curationXML);	
		
		//VT:Set Comments
		if(rsc.getRsCollection().getProjectPublication()!=null){
			resourceXML.setComments(rsc.getRsCollection().getProjectPublication());
		}
		
		Calendar cal = Calendar.getInstance();		
		cal.setTime(new Date());				
		resourceXML.setLogDate(this.objectFactory.createResourcesResourceLogDate());
		resourceXML.getLogDate().setValue(String.valueOf(cal.get(Calendar.YEAR)));
		
		if(update){
			resourceXML.getLogDate().setEventType(EventType.UPDATED);
		}else{			
			resourceXML.getLogDate().setEventType(EventType.REGISTERED);
		}
		
		return resourceXML;	
	}
	
	public  Resource register(RsSample rsc,boolean update) throws FileNotFoundException{
		Resource resourceXML = this.objectFactory.createResourcesResource();
		resourceXML.setResourceIdentifier(this.objectFactory.createResourcesResourceResourceIdentifier());
		resourceXML.getResourceIdentifier().setValue(rsc.getIgsn());
		resourceXML.setRegisteredObjectType("http://pid.geoscience.gov.au/def/voc/igsn-codelists/PhysicalSample");
		
		resourceXML.setResourceTitle(rsc.getRsSubcollection().getRsCollection().getProject() + ":" + (rsc.getCsiroSampleId()==null || rsc.getCsiroSampleId().isEmpty()?rsc.getIgsn():rsc.getCsiroSampleId()));
		
		
		resourceXML.setIsPublic(this.objectFactory.createResourcesResourceIsPublic());
		resourceXML.getIsPublic().setValue(true);
		resourceXML.setLandingPage(Config.getIGSNLanding()+"browsesamples/" + rsc.getIgsn());	
		
		resourceXML.setResourceTypes(this.objectFactory.createResourcesResourceResourceTypes());
		resourceXML.getResourceTypes().getResourceType().add("http://www.opengis.net/def/nil/OGC/0/unknown");
		
		resourceXML.setMaterialTypes(this.objectFactory.createResourcesResourceMaterialTypes());
		resourceXML.getMaterialTypes().getMaterialType().add("http://pid.geoscience.gov.au/def/voc/igsn-codelists/rock");
		
		
		if(rsc.getSampleType()!=null && !rsc.getSampleType().isEmpty()){
			resourceXML.setClassifications(this.objectFactory.createResourcesResourceClassifications());			
			Classification classificationXML = this.objectFactory.createResourcesResourceClassificationsClassification();
			classificationXML.setValue(rsc.getSampleType());
			resourceXML.getClassifications().getClassification().add(classificationXML);
		}
		
		resourceXML.setPurpose("Rock sample from rockstore");
								
		if(rsc.getLocation()!=null){						
			Location locationXML = this.objectFactory.createResourcesResourceLocation();
			locationXML.setGeometry(this.objectFactory.createResourcesResourceLocationGeometry());
			locationXML.getGeometry().setSrid("https://epsg.io/4326");
			locationXML.getGeometry().setValue(rsc.getLocation().toText());
			JAXBElement<Location> locationJAXB = this.objectFactory.createResourcesResourceLocation(locationXML);
			resourceXML.setLocation(locationJAXB);
		}				
		
	
		String sampleCollector = rsc.getSampleCollector();
		if(sampleCollector!=null && !sampleCollector.isEmpty()){				
			resourceXML.setContributors(this.objectFactory.createResourcesResourceContributors());			
			Contributor contributorXML = this.objectFactory.createResourcesResourceContributorsContributor();
			contributorXML.setContributorType("http://registry.it.csiro.au/def/isotc211/CI_RoleCode/originator");
			contributorXML.setContributorName(sampleCollector);
			resourceXML.getContributors().getContributor().add(contributorXML);
		}
		
		
		Curation curationXML = this.objectFactory.createResourcesResourceCurationDetailsCuration();
		curationXML.setCurator("CSIRO Rockstore");
		curationXML.setCuratingInstitution(this.objectFactory.createResourcesResourceCurationDetailsCurationCuratingInstitution());
		curationXML.getCuratingInstitution().setInstitutionURI("http://csiro.au");
		curationXML.getCuratingInstitution().setValue("CSIRO");
		
		resourceXML.setCurationDetails(this.objectFactory.createResourcesResourceCurationDetails());
		resourceXML.getCurationDetails().getCuration().add(curationXML);	
		
		Calendar cal = Calendar.getInstance();		
		cal.setTime(new Date());				
		resourceXML.setLogDate(this.objectFactory.createResourcesResourceLogDate());
		resourceXML.getLogDate().setValue(String.valueOf(cal.get(Calendar.YEAR)));
		
		if(update){
			resourceXML.getLogDate().setEventType(EventType.UPDATED);
		}else{			
			resourceXML.getLogDate().setEventType(EventType.REGISTERED);
		}
		//VT: Set comments
		if(rsc.getRsSubcollection().getRsCollection().getProjectPublication()!=null){
			resourceXML.setComments(rsc.getRsSubcollection().getRsCollection().getProjectPublication());
		}
				
	
		
		return resourceXML;	
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
