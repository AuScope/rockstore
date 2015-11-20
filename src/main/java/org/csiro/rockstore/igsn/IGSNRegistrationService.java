package org.csiro.rockstore.igsn;

import java.io.FileNotFoundException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.csiro.rockstore.entity.postgres.RsSample;
import org.csiro.rockstore.entity.postgres.RsSubcollection;
import org.csiro.rockstore.http.HttpServiceProvider;
import org.csiro.rockstore.utilities.Config;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class IGSNRegistrationService{

	private final Log log = LogFactory.getLog(getClass());
	
	private static IGSNRegistrationService service;
	
	private static boolean isRunning;
	
	private HttpServiceProvider httpServiceProvider;
	
	public IGSNRegistrationService(HttpServiceProvider httpServiceProvider){
		this.httpServiceProvider = httpServiceProvider;
		IGSNRegistrationService.service=null;
		isRunning=false;
	}

	
	public void register(RsSubcollection rsc) throws FileNotFoundException{
		//HttpPost post= new HttpPost(Config.getIGSNUrl() +"/igsn/test/mint");
		//Samples
		System.out.println("register subcollection");
	}
	
	public void register(RsSample rsc){
		System.out.println("register sample");
	}
	
	public  synchronized void test() throws Exception{
		isRunning=true;		
		try{			
			Thread.sleep(100000);
		}catch(Exception e){
			throw e;			
		}finally{
			isRunning=false;
		}
						
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
