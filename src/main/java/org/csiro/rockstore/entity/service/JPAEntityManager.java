package org.csiro.rockstore.entity.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAEntityManager {
	
	private static final EntityManagerFactory emFactory;

    static {
        try {
            emFactory = Persistence.createEntityManagerFactory("org.hibernate.rockstore.jpa");
        }catch(Throwable ex){
            System.err.println("Cannot create EntityManagerFactory.");
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManager createEntityManager() {
        return emFactory.createEntityManager();
    }

    public static void close(){
        emFactory.close();
    }

}
