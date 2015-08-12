package org.csiro.rockstore.web.controllers;


import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.csiro.rockstore.entity.postgres.RsCollection;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class CollectionController {

    
    @RequestMapping("addUpdate.do")
    public void addUpdate(
            @RequestParam(required = false, value ="id") String id,
            @RequestParam(required = false, value ="collectionId") String collectionId,
            @RequestParam(required = false, value ="projectId") String projectId,
            @RequestParam(required = false, value ="staffFieldManager") String staffFieldManager,
            @RequestParam(required = false, value ="staffResponsible") String staffResponsible,
            @RequestParam(required = false, value ="projectResult") String projectResult,
            @RequestParam(required = false, value ="projectPublication") String projectPublication,
            @RequestParam(required = false, value ="projectCloseDate") String projectCloseDate,
            @RequestParam(required = false, value ="availableToPublic") String availableToPublic,
            @RequestParam(required = false, value ="archiveDue") String archiveDue,            
            HttpServletResponse response)throws Exception {
    	
    	DateFormat df = DateFormat.getDateInstance();
    	
    	RsCollection rc= new RsCollection(collectionId, projectId,
    			 staffFieldManager,  staffResponsible,
    			 projectResult,  projectPublication,
    			 df.parse(projectCloseDate),  Boolean.parseBoolean(availableToPublic), df.parse(archiveDue),
    			null,null);
    	


        response.setContentType("text/html");

        
        OutputStream outputStream = response.getOutputStream();      
        IOUtils.write("<!DOCTYPE html><html><head><title>IGSN</title></head><body><h1>This is a example</h1></body></html>", outputStream);

       
        outputStream.close();
    }

  
}
