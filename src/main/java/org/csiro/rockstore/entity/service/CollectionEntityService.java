package org.csiro.rockstore.entity.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.csiro.rockstore.entity.postgres.RsCollection;
import org.csiro.rockstore.entity.postgres.RsCollectionAudit;
import org.springframework.stereotype.Service;

@Service
public class CollectionEntityService {
	
	
	public void persist(RsCollection rc){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.persist(rc);
		em.flush();
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
	
	public RsCollection searchByCollectionId(String collectionId){

		EntityManager em = JPAEntityManager.createEntityManager();
		List<RsCollection> result = em.createNamedQuery("RsCollection.findCollectionById",RsCollection.class)
	    .setParameter("collectionId", collectionId)
	    .getResultList();
		em.close();
		//VT: should only ever return 1 result as collectionid is unique
		if(result.isEmpty()){
			return null;
		}{
			return result.remove(0);
		}
	    
	}
	
	public List<RsCollectionAudit> getCollectionsAudit(String collectionId){

		EntityManager em = JPAEntityManager.createEntityManager();
		List<RsCollectionAudit> result = em.createNamedQuery("RsCollectionAudit.findCollectionById",RsCollectionAudit.class)
	    .setParameter("collectionId", collectionId)
	    .getResultList();
		em.close();
		return result;	    
	}
	
	public List<RsCollection> getAllCollections(){
		EntityManager em = JPAEntityManager.createEntityManager();
		List<RsCollection> result = em.createNamedQuery("RsCollection.getAllCollection",RsCollection.class)	    
	    .getResultList();
		em.close();
		return result;
	}
	
	public List<RsCollection> searchCollections(String project, String staffIdFieldManager, String staffidResponsible,
				String projectPublication, Integer pageNumber, Integer pageSize) {
		
		EntityManager em = JPAEntityManager.createEntityManager();
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();				
		CriteriaQuery<RsCollection> criteriaQuery = criteriaBuilder.createQuery(RsCollection.class);
		Root<RsCollection> from = criteriaQuery.from(RsCollection.class);

		
		List<Predicate> predicates =this.predicateBuilder(project,staffIdFieldManager,staffidResponsible,projectPublication,criteriaBuilder,from);
			
		CriteriaQuery<RsCollection> select = criteriaQuery.select(from).where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
	
		TypedQuery<RsCollection> typedQuery = em.createQuery(select);
		
		if(pageNumber != null && pageSize != null ){
		    typedQuery.setFirstResult((pageNumber - 1)*pageSize);
		    typedQuery.setMaxResults(pageSize);
		}
	    
		
	    List<RsCollection> result = typedQuery.getResultList();
		
		em.close();
		return result;
	}

	public Long searchCollectionsCount(String project,
			String staffIdFieldManager, String staffidResponsible,
			String projectPublication) {
		
		EntityManager em = JPAEntityManager.createEntityManager();
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		 Root<RsCollection> from = countQuery.from(RsCollection.class);
		
		List<Predicate> predicates =this.predicateBuilder(project,staffIdFieldManager,staffidResponsible,projectPublication,criteriaBuilder,from);

		CriteriaQuery<Long> select = countQuery.select(criteriaBuilder.count(from)).where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
	
		Long result = em.createQuery(select).getSingleResult();
		
		em.close();
		return result;
	}
	
	
	private List<Predicate> predicateBuilder(String project,String staffIdFieldManager,String staffidResponsible,String projectPublication,CriteriaBuilder criteriaBuilder,Root<RsCollection> from){
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (project != null && !project.isEmpty()) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.upper(from.get("project")), "%" +project.toUpperCase()+"%"));
		}

		if (staffIdFieldManager != null && !staffIdFieldManager.isEmpty()) {
			predicates.add(criteriaBuilder.equal(from.get("staffIdFieldManager"), staffIdFieldManager));
		}

		if (staffidResponsible != null && !staffidResponsible.isEmpty()) {
			predicates.add(criteriaBuilder.equal(from.get("staffidResponsible"), staffidResponsible));
		}

		if (projectPublication != null && !projectPublication.isEmpty()) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.upper(from.get("projectPublication")),  "%" + projectPublication.toUpperCase() + "%"));
		}
		
		return predicates;
	}
	
}
