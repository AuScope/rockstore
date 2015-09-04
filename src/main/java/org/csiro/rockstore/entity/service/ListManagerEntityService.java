package org.csiro.rockstore.entity.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.csiro.rockstore.entity.postgres.Staff;
import org.csiro.rockstore.entity.postgres.User;
import org.springframework.stereotype.Service;

@Service
public class ListManagerEntityService {
	
	
	public void persist(Staff staff){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.persist(staff);
		em.flush();
		em.getTransaction().commit();
	    em.close();
	    
	}
	
	public void persist(User user){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.persist(user);
		em.flush();
		em.getTransaction().commit();
	    em.close();
	    
	}
	
	public void delete(Staff staff){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.remove(staff);
		em.getTransaction().commit();
	    em.close();
	    
	}
	
	
	public void delete(User user){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.remove(user);
		em.flush();
		em.getTransaction().commit();
	    em.close();
	    
	}
	
	public Staff searchStaff(String contactName){

		EntityManager em = JPAEntityManager.createEntityManager();
		List<Staff> result = em.createNamedQuery("Staff.findStaffByName",Staff.class)
	    .setParameter("contactName", contactName)
	    .getResultList();
		em.close();
		//VT: should only ever return 1 result as collectionid is unique
		if(result.isEmpty()){
			return null;
		}{
			return result.remove(0);
		}
	    
	}
	
	public User searchUser(String contactName){

		EntityManager em = JPAEntityManager.createEntityManager();
		List<User> result = em.createNamedQuery("User.findUserByName",User.class)
	    .setParameter("contactName", contactName)
	    .getResultList();
		em.close();
		//VT: should only ever return 1 result as collectionid is unique
		if(result.isEmpty()){
			return null;
		}{
			return result.remove(0);
		}
	    
	}
	
	
	public List<User> getAllUser(Principal authUser){
		EntityManager em = JPAEntityManager.createEntityManager();
		List<User> result = null;
		if(authUser==null){			
			List<Object[]> restrictedResults = em.createQuery("SELECT c.organization, c.contactName FROM User c").getResultList();
			ArrayList<User> users= new ArrayList<User>();
			for(Object [] restrictedResult:restrictedResults){
				users.add(new User(restrictedResult[0].toString(), restrictedResult[1].toString()));
			}			
			result = users;
		}else{
			result = em.createNamedQuery("User.getAuthenticated",User.class).getResultList();
		}			    
	  
		em.close();
		return result;
	}
	
	public List<Staff> getAllStaff(Principal authUser){
		EntityManager em = JPAEntityManager.createEntityManager();
		List<Staff> result = null;
		if(authUser==null){			
			List<Object[]> restrictedResults = em.createQuery("SELECT c.organization, c.contactName FROM Staff c").getResultList();
			ArrayList<Staff> staffs= new ArrayList<Staff>();
			for(Object [] restrictedResult:restrictedResults){
				staffs.add(new Staff(restrictedResult[0].toString(), restrictedResult[1].toString()));
			}			
			result = staffs;
		}else{
			result = em.createNamedQuery("Staff.getAuthenticated",Staff.class).getResultList();
		}			    
	  
		em.close();
		return result;
	}
	
	
	
}
