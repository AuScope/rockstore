package org.csiro.rockstore.web.controllers;


import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csiro.igsn.bindings.allocation2_0.Samples;
import org.csiro.rockstore.entity.postgres.CheckoutRegistry;
import org.csiro.rockstore.entity.postgres.RsCollection;
import org.csiro.rockstore.entity.postgres.RsCollectionAudit;
import org.csiro.rockstore.entity.postgres.RsSubcollection;
import org.csiro.rockstore.entity.postgres.RsSubcollectionAudit;
import org.csiro.rockstore.entity.service.CollectionEntityService;
import org.csiro.rockstore.entity.service.SubCollectionEntityService;
import org.csiro.rockstore.igsn.IGSNRegistrationService;
import org.csiro.rockstore.security.LdapUser;
import org.csiro.rockstore.utilities.NullUtilities;
import org.csiro.rockstore.utilities.SecurityUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
 


@Controller
public class SubCollectionController {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	SubCollectionEntityService subCollectionEntityService;
	CollectionEntityService collectionEntityService;
	IGSNRegistrationService igsnService;
	
	@Autowired
	public SubCollectionController(CollectionEntityService collectionEntityService, SubCollectionEntityService subCollectionEntityService,IGSNRegistrationService igsnService){
		this.subCollectionEntityService=subCollectionEntityService;
		this.collectionEntityService =collectionEntityService;
		this.igsnService = igsnService;
	}


    @RequestMapping(value = "subCollectionAddUpdate.do")
    public ResponseEntity<Object> subCollectionAddUpdate(            
            @RequestParam(required = false, value ="subcollectionId") String subcollectionId,
            @RequestParam(required = false, value ="locationInStorage") String locationInStorage,
            @RequestParam(required = false, value ="oldId") String oldId,           
            @RequestParam(required = false, value ="storageType") String storageType,
            @RequestParam(required = false, value ="hazardous") boolean hazardous,
            @RequestParam(required = true, value ="collectionId") String collectionId,
            @RequestParam(required = false, value ="source") String source,
            @RequestParam(required = false, value ="totalPallet") Integer totalPallet,
            @RequestParam(required = false, value ="previousPalletId") String previousPalletId,
            @RequestParam(required = false, value ="disposedInsufficientInfo") boolean disposedInsufficientInfo,
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
	    	if(subcollectionId != null && !subcollectionId.isEmpty()){
	    		RsSubcollection rsc = this.subCollectionEntityService.search(subcollectionId);
	    		RsCollection rc = collectionEntityService.searchByCollectionId(collectionId);
	    		rsc.update(rc, oldId,locationInStorage, storageType, hazardous,
	    					 source,  totalPallet,user.getName(),  previousPalletId,  disposedInsufficientInfo);
	    		
	    		this.subCollectionEntityService.merge(rsc);
	    		
	    		try{//VT: update igsn on changes
		       		Samples samplesXML = new Samples();		    				    		
		       		samplesXML.getSample().add(igsnService.register(rsc,true));
		       		igsnService.mint(samplesXML);
		       	}catch(Exception e){
		       		logger.error(e);
		       	}
	    		
	    		return  new ResponseEntity<Object>(rsc,HttpStatus.OK);  
	    		
	    	}else{
	    		RsCollection rc = collectionEntityService.searchByCollectionId(collectionId);
	    		RsSubcollection rsc= new RsSubcollection(rc,  oldId,
	    				  locationInStorage,  storageType,  hazardous,
	    					 source,  totalPallet,user.getName(),  previousPalletId,  disposedInsufficientInfo);
	       	
		       	this.subCollectionEntityService.persist(rsc);
		       	
		       	try{//VT: Mint new record
		       		Samples samplesXML = new Samples();		    				    		
		       		samplesXML.getSample().add(igsnService.register(rsc,false));
		       		igsnService.mint(samplesXML);
		       	}catch(Exception e){
		       		logger.error(e);
		       	}
		
		       	return  new ResponseEntity<Object>(rsc,HttpStatus.OK);    		
	    	}    
    	}catch(Exception e){
    		logger.warn(e);
    		return new  ResponseEntity<Object>(new ExceptionWrapper("Error updating",e.getMessage()),HttpStatus.BAD_REQUEST);
    	}
    }
	
      
    
    @RequestMapping(value = "getSubCollections.do")
    public ResponseEntity<List<RsSubcollection>> getSubCollections(  
    		@RequestParam(required = false, value ="subCollectionId") String subCollectionId,
    		Principal user,
            HttpServletResponse response) {
    	try{
    		List<RsSubcollection> lrc = null;
    		if(subCollectionId != null && !subCollectionId.isEmpty()){
    			ArrayList<RsSubcollection> result= (new ArrayList<RsSubcollection>());
    			result.add(this.subCollectionEntityService.search(subCollectionId));
    			lrc = result;
    		}else{
    			lrc= this.subCollectionEntityService.getSubCollections();
    		}    		    		
    		return  new ResponseEntity<List<RsSubcollection>>(lrc,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);   
    		throw e;
    	}
    } 
    
    @RequestMapping(value = "getSubCollectionAudit.do")
    public ResponseEntity<List<RsSubcollectionAudit>> getSubCollectionAudit(
    		@RequestParam(required = true, value ="subCollectionId") String subCollectionId,
    		Principal user,
            HttpServletResponse response) throws Exception{
    	
    	if(user == null){
    		throw new AuthenticationException();
    	}
    	
    	try{    		    		
    		List<RsSubcollectionAudit>	lrc = this.subCollectionEntityService.getSubCollectionsAudit(subCollectionId);   		    		
    		return  new ResponseEntity<List<RsSubcollectionAudit>>(lrc,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);
    		throw e;
    	}

    } 
    
    @RequestMapping(value = "getSubCollectionsByIGSN.do")
    public ResponseEntity<List<RsSubcollection>> getSubCollectionsByIGSN(  
    		@RequestParam(required = false, value ="igsn") String igsn,
    		Principal user,
            HttpServletResponse response) {
    	try{    		    					    		    		
    		List<RsSubcollection> lrc = null;
    		if(igsn != null && !igsn.isEmpty()){
    			ArrayList<RsSubcollection> result= (new ArrayList<RsSubcollection>());
    			result.add(this.subCollectionEntityService.searchByIGSN(igsn));
    			lrc = result;
    		}else{
    			lrc= this.subCollectionEntityService.getSubCollections();
    		}    		    		
    		return  new ResponseEntity<List<RsSubcollection>>(lrc,HttpStatus.OK);
    		    		
    	}catch(Exception e){
    		logger.warn(e);   
    		throw e;
    	}
    }
    
    @RequestMapping(value = "getSubCollectionsByCollection.do")
    public ResponseEntity<List<RsSubcollection>> getSubCollectionsByCollection(    
    		@RequestParam(required = true, value ="collectionId") String collectionId,
    		Principal user,
            HttpServletResponse response) {
    	try{    		    	
    		List<RsSubcollection> lrc = this.subCollectionEntityService.getSubCollectionsByCollection(collectionId);    		
    		return  new ResponseEntity<List<RsSubcollection>>(lrc,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);   
    		throw e;
    	}
    } 
    
    
    
    @RequestMapping(value = "searchSubCollections.do")
    public ResponseEntity<List<RsSubcollection>> searchSubCollections(    		
    		 @RequestParam(required = false, value ="collectionId") String collectionId,
    		 @RequestParam(required = false, value ="project") String project,
    		 @RequestParam(required = false, value ="oldId") String oldId,
             @RequestParam(required = false, value ="locationInStorage") String locationInStorage,                  
             @RequestParam(required = false, value ="storageType") String storageType,            
             @RequestParam(required = false, value ="source") String source,   
             @RequestParam(required = false, value ="igsn") String igsn,
             @RequestParam(required = false, value ="pageNumber") Integer pageNumber, 
             @RequestParam(required = false, value ="pageSize") Integer pageSize, 
    		Principal user,
            HttpServletResponse response) throws Exception{
    	try{    		
    		List<RsSubcollection> lrc = null;  
    		
    		lrc = this.subCollectionEntityService.searchSubCollections(collectionId,project,oldId,locationInStorage,storageType,source,igsn,pageNumber,pageSize);
    		
    		
    		return  new ResponseEntity<List<RsSubcollection>>(lrc,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);
    		throw e;
    	}

    } 
    
    @RequestMapping(value = "searchSubCollectionsCount.do")
    public ResponseEntity<Long> searchCollectionsCount(    		
    		 @RequestParam(required = false, value ="collectionId") String collectionId,
    		 @RequestParam(required = false, value ="project") String project,
    		 @RequestParam(required = false, value ="oldId") String oldId,
             @RequestParam(required = false, value ="locationInStorage") String locationInStorage,                  
             @RequestParam(required = false, value ="storageType") String storageType,                     
             @RequestParam(required = false, value ="source") String source,
             @RequestParam(required = false, value ="igsn") String igsn,
             @RequestParam(required = false, value ="pageNumber") Integer pageNumber, 
             @RequestParam(required = false, value ="pageSize") Integer pageSize, 
    		Principal user,
            HttpServletResponse response) throws Exception{
    	try{    		
    		
    		
    		Long count = this.subCollectionEntityService.searchSubCollectionsCount(collectionId,project,oldId,locationInStorage,storageType,source,igsn);
    		
    		
    		return  new ResponseEntity<Long>(count,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);
    		throw e;
    	}
    } 
    
    
    @RequestMapping(value = "registerCheckout.do")
    public ResponseEntity<Object> registerCheckout(    		
    		 @RequestParam(required = true, value ="subcollectionId") String subcollectionId,
    		 @RequestParam(required = true, value ="dateCheckout") String dateCheckout,
    		 @RequestParam(required = true, value ="dateDueback") String dateDueback,
    		Principal user,
            HttpServletResponse response) throws Exception{
    	try{    		
    		
    		if(user==null){
        		return new  ResponseEntity<Object>(new ExceptionWrapper("Authentication Error","Not logged in"),HttpStatus.BAD_REQUEST);
        	}
    		LdapUser authUser =SecurityUtilities.getLdapUser(user);
    		CheckoutRegistry entry = new CheckoutRegistry(subcollectionId, authUser.getUsername(), authUser.getEmail(),authUser.getName(), 
    				NullUtilities.parseDateAllowNull(dateCheckout), NullUtilities.parseDateAllowNull(dateDueback), false, null);
    		this.subCollectionEntityService.registerCheckout(entry);
    		return  new ResponseEntity<Object>(entry,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);
    		throw e;
    	}
    }
    
    
    @RequestMapping(value = "checkIn.do")
    public ResponseEntity<Object> checkIn(    		
    		 @RequestParam(required = true, value ="id") int id,    		
    		Principal user,
            HttpServletResponse response) throws Exception{
    	try{    		
    		
    		if(user==null){
        		return new  ResponseEntity<Object>(new ExceptionWrapper("Authentication Error","Not logged in"),HttpStatus.BAD_REQUEST);
        	}
    		
    		CheckoutRegistry entry = this.subCollectionEntityService.checkIn(id,user);
    		return  new ResponseEntity<Object>(entry,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);
    		throw e;
    	}
    }
    
    @RequestMapping(value = "checkOut.do")
    public ResponseEntity<Object> checkOut(    		
    		 @RequestParam(required = true, value ="id") int id,    		
    		Principal user,
            HttpServletResponse response) throws Exception{
    	try{    		
    		
    		if(user==null){
        		return new  ResponseEntity<Object>(new ExceptionWrapper("Authentication Error","Not logged in"),HttpStatus.BAD_REQUEST);
        	}
    		
    		CheckoutRegistry entry = this.subCollectionEntityService.checkout(id,user);
    		return  new ResponseEntity<Object>(entry,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);
    		throw e;
    	}
    }
    
    @RequestMapping(value = "getCheckoutLogs.do")
    public ResponseEntity<List<CheckoutRegistry>> registerCheckout(    		
    		 @RequestParam(required = true, value ="subcollectionId") String subcollectionId,    		
    		Principal user,
            HttpServletResponse response) throws Exception{
    	if(user == null){
    		throw new AuthenticationException();
    	}
    	try{    		    		    		 		
    		List<CheckoutRegistry> logs = this.subCollectionEntityService.getCheckoutLogs(subcollectionId);
    		return  new ResponseEntity<List<CheckoutRegistry>>(logs,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);
    		throw e;
    	}
    }
    
    @RequestMapping(value = "getPendingEntries.do")
    public ResponseEntity<List<CheckoutRegistry>> getPendingEntries(    		    		     		
    		Principal user,
            HttpServletResponse response) throws Exception{
    	
    	if(user == null){
    		throw new AuthenticationException();
    	}
    	
    	try{    		    		    		 		
    		List<CheckoutRegistry> logs = this.subCollectionEntityService.getPending();
    		return  new ResponseEntity<List<CheckoutRegistry>>(logs,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);
    		throw e;
    	}
    }
}
