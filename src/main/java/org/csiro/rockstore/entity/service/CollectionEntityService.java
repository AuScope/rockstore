package org.csiro.rockstore.entity.service;

import java.util.List;

import javax.persistence.EntityManager;
import org.csiro.rockstore.entity.postgres.RsCollection;
import org.springframework.stereotype.Service;

@Service
public class CollectionEntityService {
	
	
	public void persist(RsCollection rc){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.persist(rc);
		em.getTransaction().commit();
	    em.close();
	    
	}
	
	public void merge(RsCollection rc){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.merge(rc);
		em.getTransaction().commit();
	    em.close();
	    
	}
	
	public RsCollection search(String collectionId){

		EntityManager em = JPAEntityManager.createEntityManager();
		List<RsCollection> result = em.createNamedQuery("RsCollection.findCollectionById",RsCollection.class)
	    .setParameter("collectionId", collectionId)
	    .getResultList();
		
		//VT: should only ever return 1 result as collectionid is unique
		return result.remove(0);
	    
	}
	
	public List<RsCollection> getCollections(){
		EntityManager em = JPAEntityManager.createEntityManager();
		List<RsCollection> result = em.createNamedQuery("RsCollection.getAllCollection",RsCollection.class)	    
	    .getResultList();
		
		return result;
	}
	
}
