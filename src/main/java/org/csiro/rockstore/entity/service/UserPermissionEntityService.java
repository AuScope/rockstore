package org.csiro.rockstore.entity.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.csiro.rockstore.entity.postgres.Staff;
import org.csiro.rockstore.entity.postgres.User;
import org.csiro.rockstore.entity.postgres.UserPermission;
import org.springframework.stereotype.Service;

@Service
public class UserPermissionEntityService {
	
	
	public void persist(UserPermission permission){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.persist(permission);
		em.flush();
		em.getTransaction().commit();
	    em.close();
	    
	}
	

	
	public void delete(UserPermission permission){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.remove(em.contains(permission) ? permission : em.merge(permission));
		em.getTransaction().commit();
	    em.close();
	    
	}
	

	public UserPermission searchUser(String staff){
		try{
			EntityManager em = JPAEntityManager.createEntityManager();
			UserPermission result = em.createNamedQuery("UserPermission.getPermissionByStaff",UserPermission.class)
		    .setParameter("staff", staff)
		    .getSingleResult();
			em.close();
			return result;
		}catch(NoResultException nre){
			return null;
		}
		
	    
	}
	
	public List<UserPermission> listPermissions(){

		EntityManager em = JPAEntityManager.createEntityManager();
		List<UserPermission> result = em.createNamedQuery("UserPermission.getPermissionList",UserPermission.class)
	    .getResultList();	    
		em.close();
		return result;
		
	    
	}
	
	
	
	
	
	
}
