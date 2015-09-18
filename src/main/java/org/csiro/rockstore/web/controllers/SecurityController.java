package org.csiro.rockstore.web.controllers;

import java.security.Principal;
import java.util.List;

import org.apache.http.auth.AuthenticationException;
import org.csiro.rockstore.entity.postgres.UserPermission;
import org.csiro.rockstore.entity.service.CollectionEntityService;
import org.csiro.rockstore.entity.service.SubCollectionEntityService;
import org.csiro.rockstore.entity.service.UserPermissionEntityService;
import org.csiro.rockstore.security.LdapUser;
import org.csiro.rockstore.utilities.SecurityUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class SecurityController {
	
	UserPermissionEntityService userPermissionEntityService;
	
	@Autowired
	public SecurityController(UserPermissionEntityService userPermissionEntityService){
		this.userPermissionEntityService = userPermissionEntityService;
	}
	
	@RequestMapping("getUser.do")
	public ResponseEntity<LdapUser>  user(Principal user) {						
		return new ResponseEntity<LdapUser>(SecurityUtilities.getLdapUser(user),HttpStatus.OK);    			
	}
	
	
	@RequestMapping("addUserPermission.do")
	public ResponseEntity<Object>  addUserPermission(
			@RequestParam(required = true, value ="staff") String staff,
			@RequestParam(required = false, defaultValue="false", value ="admin") Boolean admin,
			@RequestParam(required = false, defaultValue="false", value ="canEdit") Boolean canEdit,
			@RequestParam(required = false, defaultValue="false", value ="geologist") Boolean geologist,
			Principal user) {
		
		if(user==null){
    		return new  ResponseEntity<Object>(new ExceptionWrapper("Authentication Error","Not logged in"),HttpStatus.BAD_REQUEST);
    	}
		
		LdapUser authUser = SecurityUtilities.getLdapUser(user); 
    	if(authUser.getUserPermission()==null || authUser.getUserPermission().getAdmin()==false){
    		return new  ResponseEntity<Object>(new ExceptionWrapper("Authentication Error","Insufficient Permission"),HttpStatus.BAD_REQUEST);
    	}

		UserPermission permission = new UserPermission(staff, admin, canEdit, geologist);
		userPermissionEntityService.persist(permission);
		return new ResponseEntity<Object>(permission,HttpStatus.OK);    			
	}
	
	@RequestMapping("deleteUserPermission.do")
	public ResponseEntity<Object>  deleteUserPermission(
			@RequestParam(required = true, value ="staff") String staff,
			Principal user) {	
		
		if(user==null){
    		return new  ResponseEntity<Object>(new ExceptionWrapper("Authentication Error","Not logged in"),HttpStatus.BAD_REQUEST);
    	}
		
		LdapUser authUser = SecurityUtilities.getLdapUser(user); 
    	if(authUser.getUserPermission()==null || authUser.getUserPermission().getAdmin()==false){
    		return new  ResponseEntity<Object>(new ExceptionWrapper("Authentication Error","Insufficient Permission"),HttpStatus.BAD_REQUEST);
    	}
		
		UserPermission permission = userPermissionEntityService.searchUser(staff);
		userPermissionEntityService.delete(permission);
		return new ResponseEntity<Object>(permission,HttpStatus.OK);    			
	}
	
	@RequestMapping("listUserPermission.do")
	public ResponseEntity<List<UserPermission>>  listUserPermission(		
			Principal user) throws AuthenticationException {
		
		LdapUser authUser = SecurityUtilities.getLdapUser(user); 
    	if(authUser.getUserPermission()==null || authUser.getUserPermission().getAdmin()==false){
    		throw new AuthenticationException("Insufficient privilege"); 
    	}
		
		List<UserPermission> permissions = userPermissionEntityService.listPermissions();
		return new ResponseEntity<List<UserPermission>>(permissions,HttpStatus.OK);    			
	}

	
}
