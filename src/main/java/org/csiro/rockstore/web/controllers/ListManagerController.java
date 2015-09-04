package org.csiro.rockstore.web.controllers;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csiro.rockstore.entity.postgres.RsCollection;
import org.csiro.rockstore.entity.postgres.RsSubcollection;
import org.csiro.rockstore.entity.postgres.Staff;
import org.csiro.rockstore.entity.postgres.User;
import org.csiro.rockstore.entity.service.CollectionEntityService;
import org.csiro.rockstore.entity.service.ListManagerEntityService;
import org.csiro.rockstore.entity.service.SubCollectionEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
 


@Controller
public class ListManagerController {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	ListManagerEntityService listManagerEntityService;
	
	
	@Autowired
	public ListManagerController(ListManagerEntityService listManagerEntityService){
		this.listManagerEntityService = listManagerEntityService; 
	}
	
	@RequestMapping(value = "deleteUser.do")
    public ResponseEntity<Object> deleteUser(     		    		
    		@RequestParam(required = true, value ="contactName") String contactName,    			
    		Principal authUser,
            HttpServletResponse response) {
		
		if(authUser == null){
    		return new  ResponseEntity<Object>(new ExceptionWrapper("Error updating","unauthenticaed Access"),HttpStatus.BAD_REQUEST);
    	}
		
    	try{    		
    		User user = this.listManagerEntityService.searchUser(contactName);
    		this.listManagerEntityService.delete(user);;    		
    		return  new ResponseEntity<Object>(user,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);   
    		throw e;
    	}
    } 
    
    @RequestMapping(value = "deleteStaff.do")
    public ResponseEntity<Object> deleteStaff(    
    		@RequestParam(required = true, value ="contactName") String contactName,    	
    		Principal authUser,
            HttpServletResponse response) {
    	if(authUser == null){
    		return new  ResponseEntity<Object>(new ExceptionWrapper("Error updating","unauthenticaed Access"),HttpStatus.BAD_REQUEST);
    	}
    	try{    		    	
    		Staff staff = this.listManagerEntityService.searchStaff(contactName);
    		this.listManagerEntityService.delete(staff);;    		
    		return  new ResponseEntity<Object>(staff,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);   
    		throw e;
    	}
    } 


	@RequestMapping(value = "addUser.do")
    public ResponseEntity<Object> addUser(    
    		@RequestParam(required = true, value ="contactName") String contactName,
    		@RequestParam(required = true, value ="organization") String organization,
    		@RequestParam(required = false, value ="phone") String phone,
    		@RequestParam(required = false, value ="email") String email,
    		@RequestParam(required = false, value ="address") String address,
    		@RequestParam(required = false, value ="city") String city,
    		@RequestParam(required = false, value ="state") String state,
    		@RequestParam(required = false, value ="zipCode") String zipCode,
    		Principal authUser,
            HttpServletResponse response) {
		if(authUser == null){
    		return new  ResponseEntity<Object>(new ExceptionWrapper("Error updating","unauthenticaed Access"),HttpStatus.BAD_REQUEST);
    	}
    	try{    		    	
    		User user = new User( organization,contactName,  phone,  email, address, city, state, zipCode);  		
    		this.listManagerEntityService.persist(user);
    		return  new ResponseEntity<Object>(user,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);   
    		throw e;
    	}
    } 
    
    @RequestMapping(value = "addStaff.do")
    public ResponseEntity<Object> addStaff(    
    		@RequestParam(required = true, value ="contactName") String contactName,
    		@RequestParam(required = true, value ="organization") String organization,
    		@RequestParam(required = false, value ="phone") String phone,
    		@RequestParam(required = false, value ="email") String email,
    		@RequestParam(required = false, value ="address") String address,
    		@RequestParam(required = false, value ="city") String city,
    		@RequestParam(required = false, value ="state") String state,
    		@RequestParam(required = false, value ="zipCode") String zipCode,
    		Principal authUser,    		
            HttpServletResponse response) {
    	
    	if(authUser == null){
    		return new  ResponseEntity<Object>(new ExceptionWrapper("Error updating","unauthenticaed Access"),HttpStatus.BAD_REQUEST);
    	}
    	
    	try{    		    	
    		Staff staff = new Staff( organization,contactName,  phone,  email, address, city, state, zipCode);  		
    		this.listManagerEntityService.persist(staff);
    		return  new ResponseEntity<Object>(staff,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);   
    		throw e;
    	}
    } 
      
    
    
    @RequestMapping(value = "getUsers.do")
    public ResponseEntity<List<User>> getUsers(  
    		Principal authUser,
            HttpServletResponse response) {
    	try{    		    	
    		List<User> users = this.listManagerEntityService.getAllUser(authUser);    		
    		return  new ResponseEntity<List<User>>(users,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);   
    		throw e;
    	}
    } 
    
    @RequestMapping(value = "getStaffs.do")
    public ResponseEntity<List<Staff>> getStaffs(        		
    		Principal authUser,
            HttpServletResponse response) {
    	try{    		    	
    		List<Staff> staffs = this.listManagerEntityService.getAllStaff(authUser);    		
    		return  new ResponseEntity<List<Staff>>(staffs,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);   
    		throw e;
    	}
    } 
    
    
    
    
    
    
}
