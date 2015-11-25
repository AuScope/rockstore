package org.csiro.rockstore.igsn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class ScheduleMintIGSN  extends QuartzJobBean{

	private IGSNRegistrationService igsnRegistrationService;
	
	private final Log log = LogFactory.getLog(getClass());
	
	public ScheduleMintIGSN(){
		this.igsnRegistrationService = IGSNRegistrationService.getInstance();
	}
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		log.info("attempting the run");
		if(!igsnRegistrationService.isRunning()){
			log.info("Not running, lets run the service");
			try{
				igsnRegistrationService.run();
			}catch(Exception e){				
				log.error(e);
			}
		}else{
			log.info("The system is already running, unable to proceed. Abort!!!!!");
		}
		
	}

	public IGSNRegistrationService getIgsnRegistrationService() {
		return igsnRegistrationService;
	}

	public void setIgsnRegistrationService(IGSNRegistrationService igsnRegistrationService) {
		this.igsnRegistrationService = igsnRegistrationService;
	}

	
}
