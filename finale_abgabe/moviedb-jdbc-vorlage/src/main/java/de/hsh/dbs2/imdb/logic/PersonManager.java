package de.hsh.dbs2.imdb.logic;

import java.util.List;

import de.hsh.dbs2.imdb.util.EMFSingleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class PersonManager {


    public List<String> getPersonList(String name) throws Exception {
        EntityManager em = EMFSingleton.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<String> query = em.createQuery(
                "SELECT p.name FROM Person p WHERE p.name LIKE :search", String.class);
            query.setParameter("search", "%" + name + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
                

    public Long getPerson(String name) throws Exception {
        EntityManager em = EMFSingleton.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT p.id FROM Person p WHERE p.name = :name", Long.class);
            query.setParameter("name", name);
            
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new Exception("Person nicht gefunden: " + name);
        } finally {
            em.close();
        }
    }
}