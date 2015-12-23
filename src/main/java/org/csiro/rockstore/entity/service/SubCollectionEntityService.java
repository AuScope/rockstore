package org.csiro.rockstore.entity.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.csiro.rockstore.entity.postgres.CheckoutRegistry;
import org.csiro.rockstore.entity.postgres.RsCollection;
import org.csiro.rockstore.entity.postgres.RsSubcollection;
import org.csiro.rockstore.entity.postgres.RsSubcollectionAudit;
import org.csiro.rockstore.entity.postgres.SampleRangeBySubcollection;
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
	
	public List<RsSubcollection> getSubCollectionsByCollection(String collectionId){
		EntityManager em = JPAEntityManager.createEntityManager();
		
		List<RsSubcollection> result = em.createNamedQuery("RsSubcollection.findSubCollectionByCollection",RsSubcollection.class)
				.setParameter("collectionId", collectionId)
				.getResultList();
		em.close();
		return result;
	}
	
	public List<RsSubcollectionAudit> getSubCollectionsAudit(String subcollectionId){
		EntityManager em = JPAEntityManager.createEntityManager();
		
		List<RsSubcollectionAudit> result = em.createNamedQuery("RsSubcollectionAudit.findSubCollectionById",RsSubcollectionAudit.class)
				.setParameter("subcollectionId", subcollectionId)
				.getResultList();
		em.close();
		return result;
	}

	public List<RsSubcollection> searchSubCollections(String collectionId,String project,String oldId,
			String locationInStorage, String storageType, String source,String igsn, Integer pageNumber, Integer pageSize) {
		
		EntityManager em = JPAEntityManager.createEntityManager();
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();				
		CriteriaQuery<RsSubcollection> criteriaQuery = criteriaBuilder.createQuery(RsSubcollection.class);
		Root<RsSubcollection> from = criteriaQuery.from(RsSubcollection.class);
		
		from.fetch("rsCollection");
		from.fetch("sampleRangeBySubcollection",JoinType.LEFT);
					
		List<Predicate> predicates =this.predicateBuilder(collectionId,project,oldId,locationInStorage,storageType,source,igsn, criteriaBuilder,from);
			
		CriteriaQuery<RsSubcollection> select = criteriaQuery.select(from).where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
		
		select = select.orderBy(criteriaBuilder.asc(from.get("subcollectionId")));
	
		TypedQuery<RsSubcollection> typedQuery = em.createQuery(select);
		
		if(pageNumber != null && pageSize != null){
			typedQuery.setFirstResult((pageNumber - 1)*pageSize);
		    typedQuery.setMaxResults(pageSize);
		}

	    List<RsSubcollection> result = typedQuery.getResultList();
		
		em.close();
		return result;
	}

	public Long searchSubCollectionsCount(String collectionId,String project,String oldId,
			String locationInStorage, String storageType, String source, String igsn) {
		
		EntityManager em = JPAEntityManager.createEntityManager();
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		 Root<RsSubcollection> from = countQuery.from(RsSubcollection.class);		
		
		 List<Predicate> predicates =this.predicateBuilder(collectionId,project,oldId,locationInStorage,storageType,source,igsn, criteriaBuilder,from);

		CriteriaQuery<Long> select = countQuery.select(criteriaBuilder.count(from)).where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
	
		Long result = em.createQuery(select).getSingleResult();
		
		em.close();
		return result;
	}
	
	
	private List<Predicate> predicateBuilder(String collectionId,String project,String oldId,String locationInStorage,String storageType,String source,String igsn,CriteriaBuilder criteriaBuilder,Root<RsSubcollection> from){
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (collectionId != null && !collectionId.isEmpty()) {
			predicates.add(criteriaBuilder.equal(from.get("rsCollection").get("collectionId"), collectionId.toUpperCase()));
		}
		
		if (project != null && !project.isEmpty()) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.upper(from.get("rsCollection").get("project")),  "%"+ project.toUpperCase() +"%"));
		}
		
		if (oldId != null && !oldId.isEmpty()) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.upper(from.get("oldId")),  "%"+ oldId.toUpperCase() +"%"));
		}

		if (locationInStorage != null && !locationInStorage.isEmpty()) {
			predicates.add(criteriaBuilder.equal(from.get("locationInStorage"), locationInStorage));
		}

		if (storageType != null && !storageType.isEmpty()) {
			predicates.add(criteriaBuilder.equal(from.get("storageType"), storageType));
		}				
		
		if (source != null && !source.isEmpty()) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.upper(from.get("source")),  "%"+ source.toUpperCase() +"%"));
		}
		
		if (igsn != null && !igsn.isEmpty()) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.upper(from.get("igsn")),  "%"+ igsn.toUpperCase() +"%"));
		}
		
		
		return predicates;
	}

	public RsSubcollection searchByIGSN(String igsn) {
		EntityManager em = JPAEntityManager.createEntityManager();
		try{			
			RsSubcollection result = em.createNamedQuery("RsSubcollection.findSubCollectionByIGSN",RsSubcollection.class)
		    .setParameter("igsn", igsn)
		    .getSingleResult();
			
		return result;
		}catch(NoResultException  e){
			return null;
		}finally{
			em.close();
		}
	}

	public void registerCheckout(CheckoutRegistry entry) {
		
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.persist(entry);
		em.flush();
		em.getTransaction().commit();
	    em.close();
		
	}

	public List<CheckoutRegistry> getCheckoutLogs(String subcollectionId) {
		
		EntityManager em = JPAEntityManager.createEntityManager();		
		List<CheckoutRegistry> result = em.createNamedQuery("CheckoutRegistry.findCheckoutRegistryBySubcollectionId",CheckoutRegistry.class)
				.setParameter("subcollectionId", subcollectionId)
				.getResultList();
		em.close();
		return result;
	}

	public CheckoutRegistry checkIn(int id,Principal checkInBy) {
		// CheckoutRegistry.findCheckoutRegistryById
		EntityManager em = JPAEntityManager.createEntityManager();	
		em.getTransaction().begin();
		CheckoutRegistry result = em.createNamedQuery("CheckoutRegistry.findCheckoutRegistryById",CheckoutRegistry.class)
				.setParameter("id", id)
				.getSingleResult();
		result.setDateCheckin(new Date());
		result.setCheckinby(checkInBy.getName());
		result.setCheckoutStatus(false);			
		em.merge(result);
		em.getTransaction().commit();	
		em.close();
		return result;
		
	}
	
	public CheckoutRegistry checkout(int id,Principal checkOutBy) {
		// CheckoutRegistry.findCheckoutRegistryById
		EntityManager em = JPAEntityManager.createEntityManager();	
		em.getTransaction().begin();
		CheckoutRegistry result = em.createNamedQuery("CheckoutRegistry.findCheckoutRegistryById",CheckoutRegistry.class)
				.setParameter("id", id)
				.getSingleResult();
		result.setCheckoutStatus(true);	
		result.setCheckoutby(checkOutBy.getName());
		em.merge(result);
		em.getTransaction().commit();
		em.close();
		return result;
		
	}
	
	public List<CheckoutRegistry> getPending() {
		// CheckoutRegistry.findCheckoutRegistryById
		EntityManager em = JPAEntityManager.createEntityManager();		
		List<CheckoutRegistry> result = em.createNamedQuery("CheckoutRegistry.findPendingCheckouts",CheckoutRegistry.class)				
				.getResultList();		
		em.close();
		return result;
		
	}
	
	
	public List<RsSubcollection> getUnminted(){
		EntityManager em = JPAEntityManager.createEntityManager();
		try{			
			List<RsSubcollection> result = em.createNamedQuery("RsSubcollection.getUnminted",RsSubcollection.class)	 
			.setMaxResults(20)		
		    .getResultList();			
			return result;
		}catch(NoResultException e){							
			return null;
		}catch(Exception e){
			throw e;
		}finally{
			em.close();
		}
	    
	}
	
}
