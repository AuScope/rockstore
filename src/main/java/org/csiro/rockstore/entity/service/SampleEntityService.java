package org.csiro.rockstore.entity.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.csiro.rockstore.entity.postgres.RsSample;
import org.springframework.stereotype.Service;

@Service
public class SampleEntityService {
	
	
	public void persist(RsSample rs){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.persist(rs);
		em.getTransaction().commit();
	    em.close();
	    
	}
	
	public void merge(RsSample rs){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.merge(rs);
		em.getTransaction().commit();
	    em.close();
	    
	}
	
	public RsSample search(String sampleId){

		EntityManager em = JPAEntityManager.createEntityManager();
		List<RsSample> result = em.createNamedQuery("RsSample.findSampleById",RsSample.class)
	    .setParameter("id", sampleId)
	    .getResultList();
		
		//VT: should only ever return 1 result as collectionid is unique
		return result.remove(0);
	    
	}
	
	public List<RsSample> getAllSamples(){
		EntityManager em = JPAEntityManager.createEntityManager();
		List<RsSample> result = em.createNamedQuery("RsSample.getAllSample",RsSample.class)	    
	    .getResultList();
		
		return result;
	}
	
}
