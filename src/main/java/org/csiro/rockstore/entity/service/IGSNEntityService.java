package org.csiro.rockstore.entity.service;

import javax.persistence.EntityManager;

import org.csiro.rockstore.entity.postgres.IGSNLog;
import org.springframework.stereotype.Service;


@Service
public class IGSNEntityService {
	
	
	public void persist(IGSNLog log){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.persist(log);
		em.flush();
		em.getTransaction().commit();
	    em.close();
	    
	}
	


	
}
