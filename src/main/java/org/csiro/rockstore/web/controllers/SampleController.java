package org.csiro.rockstore.web.controllers;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csiro.igsn.bindings.allocation2_0.Samples;
import org.csiro.rockstore.entity.postgres.ImportBatch;
import org.csiro.rockstore.entity.postgres.ImportLog;
import org.csiro.rockstore.entity.postgres.RsCollection;
import org.csiro.rockstore.entity.postgres.RsSample;
import org.csiro.rockstore.entity.postgres.RsSampleAudit;
import org.csiro.rockstore.entity.postgres.RsSubcollection;
import org.csiro.rockstore.entity.postgres.RsSubcollectionAudit;
import org.csiro.rockstore.entity.service.CollectionEntityService;
import org.csiro.rockstore.entity.service.ExcelImportService;
import org.csiro.rockstore.entity.service.SampleEntityService;
import org.csiro.rockstore.entity.service.SubCollectionEntityService;
import org.csiro.rockstore.exceptions.ImportExceptions;
import org.csiro.rockstore.igsn.IGSNRegistrationService;
import org.csiro.rockstore.security.LdapUser;
import org.csiro.rockstore.utilities.NullUtilities;
import org.csiro.rockstore.utilities.SecurityUtilities;
import org.csiro.rockstore.utilities.SpatialUtilities;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.vividsolutions.jts.geom.Point;



@Controller
public class SampleController {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	SubCollectionEntityService subCollectionEntityService;
	CollectionEntityService collectionEntityService;
	SampleEntityService sampleEntityService;
	IGSNRegistrationService igsnService;
	
	@Autowired
	public SampleController(CollectionEntityService collectionEntityService, SubCollectionEntityService subCollectionEntityService,SampleEntityService sampleEntityService,IGSNRegistrationService igsnService){
		this.subCollectionEntityService=subCollectionEntityService;
		this.collectionEntityService =collectionEntityService;
		this.sampleEntityService = sampleEntityService;
		this.igsnService = igsnService;
	}


    @RequestMapping(value = "sampleAddUpdate.do")
    public ResponseEntity<Object> sampleAddUpdate(            
            @RequestParam(required = false, defaultValue="0", value ="id") int id,
            @RequestParam(required = true, value ="subcollectionId") String subcollectionId,              
            @RequestParam(required = false, value ="csiroSampleId") String csiroSampleId,           
            @RequestParam(required = false, value ="sampleType") String sampleType,
            @RequestParam(required = false, value ="bhid") String bhid,
            @RequestParam(required = false, value ="depth") Double depth,
            @RequestParam(required = false, value ="datum") String datum,           
            @RequestParam(required = false, value ="containerId") String containerId,
            @RequestParam(required = false, value ="externalRef") String externalRef,
            @RequestParam(required = false, value ="sampleCollector") String sampleCollector,
            @RequestParam(required = false, value ="dateSampled") String dateSampled,
            @RequestParam(required = false, value ="sampleDispose") Boolean sampleDispose,
            @RequestParam(required = false, value ="dateDisposed") String dateDisposed,
            @RequestParam(required = false, value ="staffidDisposed") String staffidDisposed,            
            @RequestParam(required = false, value ="origLat") String origLat,
            @RequestParam(required = false, value ="origLon") String origLon,
            Principal user,
            HttpServletResponse response){
    	
    	if(user==null){
    		return new  ResponseEntity<Object>(new ExceptionWrapper("Authentication Error","Not logged in"),HttpStatus.BAD_REQUEST);
    	}
    	
    	LdapUser authUser = SecurityUtilities.getLdapUser(user); 
    	if(authUser.getUserPermission()==null || authUser.getUserPermission().getCanEdit()==false){
    		return new  ResponseEntity<Object>(new ExceptionWrapper("Authentication Error","Insufficient Permission"),HttpStatus.BAD_REQUEST);
    	}
    	
    
    	try{
	    	if(id != 0){//VT: if id exist, we want to update else insert
	    		RsSubcollection rsc = this.subCollectionEntityService.search(subcollectionId);	    		
	    		RsSample rs = this.sampleEntityService.search(id);
	    		
	    		Point p = (Point)(SpatialUtilities.wktToGeometry(origLat, origLon ,datum));
	    		
	    		rs.update(rsc,  csiroSampleId,
	    				 sampleType,  bhid,  depth,  datum,
	    				   containerId,  externalRef,
	    				 sampleCollector,NullUtilities.parseDateAllowNull(dateSampled) , sampleDispose,
	    				 NullUtilities.parseDateAllowNull(dateDisposed),  staffidDisposed, p, origLat, origLon,user.getName());
	    		
	    		this.sampleEntityService.merge(rs);
	    		
	    		return  new ResponseEntity<Object>(rs,HttpStatus.OK);  
	    		
	    		
	    	}else{
	    		
	    		RsSubcollection rsc = subCollectionEntityService.search(subcollectionId);
	    		
	    		Point p = (Point)(SpatialUtilities.wktToGeometry(origLat, origLon,datum));
	    		
	    		RsSample rs= new RsSample(rsc,  csiroSampleId,
	    				 sampleType,  bhid,  depth,  datum,
	    				   containerId,  externalRef,
	    				 sampleCollector,NullUtilities.parseDateAllowNull(dateSampled) , sampleDispose,
	    				 NullUtilities.parseDateAllowNull(dateDisposed),  staffidDisposed, p, origLat, origLon, user.getName());
	    		
	    		
		       	this.sampleEntityService.persist(rs);
		       	
		       	try{
		       		Samples samplesXML = new Samples();		    				    		
		       		samplesXML.getSample().add(igsnService.register(rs));
		       		igsnService.mint(samplesXML);
		       	}catch(Exception e){
		       		logger.error(e);
		       	}
		
		       	return  new ResponseEntity<Object>(rs,HttpStatus.OK);    		
	    	}    
    	}catch(Exception e){
    		logger.warn(e);
    		return new  ResponseEntity<Object>(new ExceptionWrapper("Error updating",e.getMessage()),HttpStatus.BAD_REQUEST);
    	}
    }
	
      
    
    @RequestMapping(value = "getSample.do")
    public ResponseEntity<List<RsSample>> getSample(   
    		@RequestParam(required = false, defaultValue="0", value ="id") int id,
            HttpServletResponse response) {
    	try{
    		List<RsSample> lrs =  null;
    		if(id != 0){
    			ArrayList<RsSample> result= (new ArrayList<RsSample>());
    			result.add(this.sampleEntityService.search(id));
    			lrs = result;
    		}else{
    			lrs = this.sampleEntityService.getAllSamples();
    		}
    			  		
    		return  new ResponseEntity<List<RsSample>>(lrs,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);   
    		throw e;
    	}
    } 
    
    @RequestMapping(value = "getSamplesbySubCollection.do")
    public ResponseEntity<List<RsSample>> getSamplesbySubCollection(    
    		@RequestParam(required = true, value ="subCollectionId") String subCollectionId,
    		Principal user,
            HttpServletResponse response) {
    	try{    		    	
    		List<RsSample> lrc = this.sampleEntityService.getSampleBySubCollections(subCollectionId);    		
    		return  new ResponseEntity<List<RsSample>>(lrc,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);   
    		throw e;
    	}
    } 
    
    @RequestMapping(value = "getSampleAudit.do")
    public ResponseEntity<List<RsSampleAudit>> getSampleAudit(
    		@RequestParam(required = true, value ="id") int id,
    		Principal user,
            HttpServletResponse response) throws Exception{
    	
    	if(user == null){
    		throw new AuthenticationException();
    	}
    	
    	
    	try{    		    		
    		List<RsSampleAudit>	lrc = this.sampleEntityService.getSampleAudit(id);   		    		
    		return  new ResponseEntity<List<RsSampleAudit>>(lrc,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);
    		throw e;
    	}

    } 
    
    
    @RequestMapping(value = "searchSample.do")
    public ResponseEntity<List<RsSample>> searchSample(    		    		
            @RequestParam(required = false, value ="subcollectionId") String subcollectionId,           
            @RequestParam(required = false, value ="igsn") String igsn,
            @RequestParam(required = false, value ="csiroSampleId") String csiroSampleId,                       
            @RequestParam(required = false, value ="bhid") String bhid,                     
            @RequestParam(required = false, value ="externalRef") String externalRef,            
            @RequestParam(required = false, value ="pageNumber") Integer pageNumber, 
            @RequestParam(required = false, value ="pageSize") Integer pageSize, 
    		Principal user,
            HttpServletResponse response) throws Exception{
    	try{    		
    		List<RsSample> lrc = null;  
    		
    		lrc = this.sampleEntityService.searchSample(subcollectionId,igsn,csiroSampleId,bhid,externalRef,pageNumber,pageSize);

    		return  new ResponseEntity<List<RsSample>>(lrc,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);
    		throw e;
    	}

    } 
    
    @RequestMapping(value = "getSampleByIGSN.do")
    public ResponseEntity<List<RsSample>> getSampleByIGSN(  
    		@RequestParam(required = false, value ="igsn") String igsn,
    		Principal user,
            HttpServletResponse response) {
    	try{    		    					
    		List<RsSample> lrs =  null;
    		if(igsn != null && !igsn.isEmpty()){
    			ArrayList<RsSample> result= (new ArrayList<RsSample>());
    			result.add(this.sampleEntityService.searchByIGSN(igsn));
    			lrs = result;
    		}else{
    			lrs = this.sampleEntityService.getAllSamples();
    		}    			  		
    		return  new ResponseEntity<List<RsSample>>(lrs,HttpStatus.OK);    		
    	}catch(Exception e){
    		logger.warn(e);   
    		throw e;
    	}
    }
    
    @RequestMapping(value = "searchSampleCount.do")
    public ResponseEntity<Long> searchSampleCount(    		
    		 @RequestParam(required = false, value ="subcollectionId") String subcollectionId,           
             @RequestParam(required = false, value ="igsn") String igsn,
             @RequestParam(required = false, value ="csiroSampleId") String csiroSampleId,                       
             @RequestParam(required = false, value ="bhid") String bhid,                     
             @RequestParam(required = false, value ="externalRef") String externalRef, 
             @RequestParam(required = false, value ="pageNumber") Integer pageNumber, 
             @RequestParam(required = false, value ="pageSize") Integer pageSize, 
    		Principal user,
            HttpServletResponse response) throws Exception{
    	try{    		    		
    		Long count = this.sampleEntityService.searchSampleCount(subcollectionId,igsn,csiroSampleId,bhid,externalRef);    		    		
    		return  new ResponseEntity<Long>(count,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);
    		throw e;
    	}

    } 
    
    @RequestMapping(value = "preview.do")
    public ResponseEntity preview(@RequestParam("file") MultipartFile file,
    		Principal user) {

        byte[] bytes;
        
        try{
        	if (!file.isEmpty()) {
            	
            	File convFile = new File( System.getProperty("java.io.tmpdir"),file.getOriginalFilename());
            	System.out.print("Saving file to " + convFile.getAbsolutePath() );
                file.transferTo(convFile);
                            	
            	 FileInputStream fis = new FileInputStream(convFile);
        		 ExcelImportService s= new ExcelImportService();
        		
        		 List<RsSample> samples = s.generatePreview(fis,user.getName());
        		 
        		 return new ResponseEntity<List<RsSample>>(samples,HttpStatus.OK);
        		
            }
            
            return null;
        }catch(ImportExceptions ie){
        	return new  ResponseEntity<Object>(new ExceptionWrapper("Error generating preview",ie.getImportError() +"-> Column:" + ie.getColumn() + "-> Row:" + ie.getRow()),HttpStatus.BAD_REQUEST);
        }catch(Exception e){
        	return new  ResponseEntity<Object>(new ExceptionWrapper("UnExpected Exception",e.getMessage()),HttpStatus.BAD_REQUEST);
        }

    }
    
    @RequestMapping(value = "import.do")
    public ResponseEntity importExcel(@RequestParam("file") MultipartFile file,
    		Principal user) {

        byte[] bytes;
        
        try{
        	if (!file.isEmpty()) {
            	
        		File convFile = new File( System.getProperty("java.io.tmpdir"),file.getOriginalFilename());
            	System.out.print("Saving file to " + convFile.getAbsolutePath() );
                file.transferTo(convFile);
                
            	 FileInputStream fis = new FileInputStream(convFile);
        		 ExecutorService executor = Executors.newSingleThreadExecutor();

        		// somewhere else:
        		executor.execute(new ImportExcelTask(fis,user.getName())); // myRunnableTask is a Runnable

        		
            }
            
            return null;
        }catch(Exception e){
        	//VT:It should never come here. If it does, we have a serious problem
        	return new  ResponseEntity<Object>(new ExceptionWrapper("Unexpected Exception",e.getMessage()),HttpStatus.BAD_REQUEST);
        }

    }
    
    private class ImportExcelTask implements Runnable{
    	
    	FileInputStream fis;
    	String user;
    	
    	public ImportExcelTask(FileInputStream fis,String user){
    		super();
    		this.fis =fis;
    		this.user = user;
    	}

		@Override
		public void run() {
			ExcelImportService s= new ExcelImportService();
			try {
				s.processExcel(fis,user);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
    	
    }
    
    
    @RequestMapping(value = "listBatch.do")
    public ResponseEntity<List<ImportBatch>> listBatch(    	
    		Principal user,
            HttpServletResponse response) throws Exception{
    	
    	if(user == null){
    		throw new AuthenticationException();
    	}
    	
    	
    	try{    		    		
    		List<ImportBatch>	lrc = this.sampleEntityService.listBatch(user);   		    		
    		return  new ResponseEntity<List<ImportBatch>>(lrc,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);
    		throw e;
    	}

    } 
    
    @RequestMapping(value = "listBatchError.do")
    public ResponseEntity<List<ImportLog>> listBatchError(
    		@RequestParam(required = true, value ="batchId") int batchId,
    		Principal user,
            HttpServletResponse response) throws Exception{
    	
    	if(user == null){
    		throw new AuthenticationException();
    	}
    	
    	
    	try{    		    		
    		List<ImportLog>	lrc = this.sampleEntityService.listBatchLog(batchId);   		    		
    		return  new ResponseEntity<List<ImportLog>>(lrc,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);
    		throw e;
    	}

    } 
    
   
}
