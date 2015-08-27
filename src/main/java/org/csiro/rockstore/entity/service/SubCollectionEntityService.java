package org.csiro.rockstore.entity.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.csiro.rockstore.entity.postgres.RsCollection;
import org.csiro.rockstore.entity.postgres.RsSubcollection;
import org.springframework.stereotype.Service;

@Service
public class SubCollectionEntityService {
	
	
	public void persist(RsSubcollection rc){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.persist(rc);
		em.flush();
		em.getTransaction().commit();
	    em.close();
	    
	}
	
	public void merge(RsSubcollection rc){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.merge(rc);
		em.getTransaction().commit();
	    em.close();
	    
	}
	
	public RsSubcollection search(String subCollectionId){

		EntityManager em = JPAEntityManager.createEntityManager();
		List<RsSubcollection> result = em.createNamedQuery("RsSubcollection.findSubCollectionById",RsSubcollection.class)
	    .setParameter("subcollectionId", subCollectionId)
	    .getResultList();
		em.close();
		//VT: should only ever return 1 result as collectionid is unique
		return result.remove(0);
	    
	}
	
	public List<RsSubcollection> getSubCollections(){
		EntityManager em = JPAEntityManager.createEntityManager();
		List<RsSubcollection> result = em.createNamedQuery("RsSubcollection.getAllSubCollection",RsSubcollection.class)	    
	    .getResultList();
		em.close();
		return result;
	}
	
	public List<RsSubcollection> getSubCollections(String collectionId){
		EntityManager em = JPAEntityManager.createEntityManager();
		
		List<RsSubcollection> result = em.createNamedQuery("RsSubcollection.findSubCollectionByCollection",RsSubcollection.class)
				.setParameter("collectionId", collectionId)
				.getResultList();
		em.close();
		return result;
	}
	
}
