package org.csiro.rockstore.entity.service;

import javax.persistence.EntityManager;

import org.csiro.rockstore.entity.postgres.ImportBatch;
import org.csiro.rockstore.entity.postgres.ImportLog;
import org.csiro.rockstore.entity.postgres.RsCollection;
import org.springframework.stereotype.Service;

@Service
public class ImportEntityService {
	
	public void persist(ImportBatch batch){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.persist(batch);
		em.flush();
		em.getTransaction().commit();
	    em.close();	    
	}
	
	public void merge(ImportBatch batch){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.merge(batch);
		em.getTransaction().commit();
	    em.close();
	    
	}
	
	public void persist(ImportLog log){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.persist(log);
		em.flush();
		em.getTransaction().commit();
	    em.close();	    
	}
	
	public void merge(ImportLog log){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.merge(log);
		em.getTransaction().commit();
	    em.close();
	    
	}

}
