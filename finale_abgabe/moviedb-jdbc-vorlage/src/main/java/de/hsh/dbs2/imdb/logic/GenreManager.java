package de.hsh.dbs2.imdb.logic;

import java.util.HashSet;
import java.util.List;

import de.hsh.dbs2.imdb.Model.Genre;
import de.hsh.dbs2.imdb.Model.Movie;
import de.hsh.dbs2.imdb.util.EMFSingleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class GenreManager {

    public List<String> getGenres() throws Exception {
        EntityManager em = EMFSingleton.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<String> query = em.createQuery("SELECT g.genre FROM Genre g ORDER BY g.genre ASC", String.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }


    public HashSet<String> getGenresByMovie(Long movieID) throws Exception {
        EntityManager em = EMFSingleton.getEntityManagerFactory().createEntityManager();
        try {
            Movie movie = em.find(Movie.class, movieID);
            
            HashSet<String> result = new HashSet<>();
            if (movie != null) {
                for (Genre g : movie.getGenres()) {
                    result.add(g.getGenre());
                }
            }
            return result;
        } finally {
            em.close();
        }
    }
}