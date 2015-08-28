package org.csiro.rockstore.entity.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.csiro.rockstore.entity.postgres.RsSample;
import org.csiro.rockstore.entity.postgres.RsSubcollection;
import org.springframework.stereotype.Service;

@Service
public class SampleEntityService {
	
	
	public void persist(RsSample rs){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.persist(rs);
		em.flush();
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
	
	public RsSample search(int sampleId){

		EntityManager em = JPAEntityManager.createEntityManager();
		List<RsSample> result = em.createNamedQuery("RsSample.findSampleById",RsSample.class)
	    .setParameter("id", sampleId)
	    .getResultList();
		 em.close();
		//VT: should only ever return 1 result as collectionid is unique
		return result.remove(0);
	    
	}
	
	public List<RsSample> getAllSamples(){
		EntityManager em = JPAEntityManager.createEntityManager();
		List<RsSample> result = em.createNamedQuery("RsSample.getAllSample",RsSample.class)	    
	    .getResultList();
		em.close();
		return result;
	}

	public List<RsSample> getSampleBySubCollections(String subCollectionId) {
		EntityManager em = JPAEntityManager.createEntityManager();
		
		List<RsSample> result = em.createNamedQuery("RsSample.findSampleBySubCollection",RsSample.class)
				.setParameter("subCollectionId", subCollectionId)
				.getResultList();
		em.close();
		return result;
	}
	
}
