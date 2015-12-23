package org.csiro.rockstore.entity.service;

import java.security.Principal;
import java.util.ArrayList;
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

import org.csiro.rockstore.entity.postgres.ImportBatch;
import org.csiro.rockstore.entity.postgres.ImportLog;
import org.csiro.rockstore.entity.postgres.RsCollectionAudit;
import org.csiro.rockstore.entity.postgres.RsSample;
import org.csiro.rockstore.entity.postgres.RsSampleAudit;
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
	
	public List<RsSampleAudit> getSampleAudit(int sampleId){
		EntityManager em = JPAEntityManager.createEntityManager();
		List<RsSampleAudit> result = em.createNamedQuery("RsSampleAudit.findSampleById",RsSampleAudit.class)
		.setParameter("id", sampleId)
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

	public List<RsSample> searchSample(String subcollectionId, String igsn,
			String csiroSampleId, String bhid, String externalRef,
			Integer pageNumber, Integer pageSize) {
		
		EntityManager em = JPAEntityManager.createEntityManager();
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();				
		CriteriaQuery<RsSample> criteriaQuery = criteriaBuilder.createQuery(RsSample.class);
		Root<RsSample> from = criteriaQuery.from(RsSample.class);
		
		Fetch<RsSample,RsSubcollection> fetch = from.fetch("rsSubcollection");
		fetch.fetch("rsCollection");
		fetch.fetch("sampleRangeBySubcollection",JoinType.LEFT);
					
		List<Predicate> predicates =this.predicateBuilder(subcollectionId,igsn,csiroSampleId,bhid,externalRef, criteriaBuilder,from);
			
		CriteriaQuery<RsSample> select = criteriaQuery.select(from).where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
		select = select.orderBy(criteriaBuilder.asc(from.get("rsSubcollection").get("subcollectionId")));
		
	
		TypedQuery<RsSample> typedQuery = em.createQuery(select);
		
		if(pageNumber != null && pageSize != null){
		    typedQuery.setFirstResult((pageNumber - 1)*pageSize);
		    typedQuery.setMaxResults(pageSize);
		}
	    
		
	    List<RsSample> result = typedQuery.getResultList();
		
		em.close();
		return result;
	}

	public Long searchSampleCount(String subcollectionId, String igsn,String csiroSampleId,
			String bhid, String externalRef) {
		
		EntityManager em = JPAEntityManager.createEntityManager();
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		 Root<RsSample> from = countQuery.from(RsSample.class);
		
		
		 List<Predicate> predicates =this.predicateBuilder(subcollectionId,igsn,csiroSampleId,bhid,externalRef, criteriaBuilder,from);

		CriteriaQuery<Long> select = countQuery.select(criteriaBuilder.count(from)).where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
	
		Long result = em.createQuery(select).getSingleResult();
		
		em.close();
		return result;
	}
	
	private List<Predicate> predicateBuilder(String subcollectionId,String igsn,String csiroSampleId,String bhid,String externalRef,CriteriaBuilder criteriaBuilder,Root<RsSample> from){
				
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (subcollectionId != null && !subcollectionId.isEmpty()) {
			predicates.add(criteriaBuilder.equal(from.get("rsSubcollection").get("subcollectionId"), subcollectionId.toUpperCase()));
		}					
		
		if (igsn != null && !igsn.isEmpty()) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.upper(from.get("igsn")),  "%"+ igsn.toUpperCase() +"%"));
		}
		
		if (csiroSampleId != null && !csiroSampleId.isEmpty()) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.upper(from.get("csiroSampleId")),  "%"+ csiroSampleId.toUpperCase() +"%"));
		}
		
		if (bhid != null && !bhid.isEmpty()) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.upper(from.get("bhid")),  "%"+ bhid.toUpperCase() +"%"));
		}
		
		if (externalRef != null && !externalRef.isEmpty()) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.upper(from.get("externalRef")),  "%"+ externalRef.toUpperCase() +"%"));
		}		
		
		return predicates;
	}

	public RsSample searchByIGSN(String igsn) {
		EntityManager em = JPAEntityManager.createEntityManager();
		try{					
			RsSample result = em.createNamedQuery("RsSample.findSampleByIGSN",RsSample.class)
		    .setParameter("igsn", igsn)
		    .getSingleResult();		
		return result;
		}catch(NoResultException e){
			return null;
		}finally{
			em.close();
		}
	}

	public List<ImportBatch> listBatch(Principal user) {
		EntityManager em = JPAEntityManager.createEntityManager();
		List<ImportBatch> result = em.createNamedQuery("ImportBatch.listBatch",ImportBatch.class)
	    .setParameter("user", user.getName())
	    .getResultList();
		 em.close();
		//VT: should only ever return 1 result as collectionid is unique
		return result;
	}

	public List<ImportLog> listBatchLog(int batchId) {
		EntityManager em = JPAEntityManager.createEntityManager();
		List<ImportLog> result = em.createNamedQuery("ImportLog.listBatchLog",ImportLog.class)
	    .setParameter("batchId", batchId)
	    .getResultList();
		 em.close();
		//VT: should only ever return 1 result as collectionid is unique
		return result;
	}
	
	public List<RsSample> getUnminted(){

		EntityManager em = JPAEntityManager.createEntityManager();
		try{
			List<RsSample> result = em.createNamedQuery("RsSample.getUnminted",RsSample.class)	   
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
