package org.csiro.rockstore.igsn;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class ScheduleMintIGSN  extends QuartzJobBean{

	private IGSNRegistrationService igsnRegistrationService;
	
	public ScheduleMintIGSN(){
		this.igsnRegistrationService = IGSNRegistrationService.getInstance();
	}
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println("attempting the run");
		if(!igsnRegistrationService.isRunning()){
			System.out.println("Not running, lets run the service");
			try{
				igsnRegistrationService.test();
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("Running!!!!,Cancel the request");
		}
		
	}

	public IGSNRegistrationService getIgsnRegistrationService() {
		return igsnRegistrationService;
	}

	public void setIgsnRegistrationService(IGSNRegistrationService igsnRegistrationService) {
		this.igsnRegistrationService = igsnRegistrationService;
	}

	
}
