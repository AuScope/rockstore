package org.csiro.rockstore.web.controllers;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class SecurityController {
	
	@RequestMapping("getUser.do")
	public ResponseEntity<Principal>  user(Principal user) {
		return new ResponseEntity<Principal>(user,HttpStatus.OK);    			
	}

	
}
