package de.hsh.dbs2.imdb.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.hsh.dbs2.imdb.Model.Genre;
import de.hsh.dbs2.imdb.Model.Movie;
import de.hsh.dbs2.imdb.Model.MovieCharacter;
import de.hsh.dbs2.imdb.Model.Person;
import de.hsh.dbs2.imdb.logic.dto.CharacterDTO;
import de.hsh.dbs2.imdb.logic.dto.MovieDTO;
import de.hsh.dbs2.imdb.util.EMFSingleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class MovieManager {


    private MovieDTO mapEntityToDTO(Movie movie) {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(movie.getId()); 
        movieDTO.setTitle(movie.getTitle());
        movieDTO.setYear(movie.getYear());
        movieDTO.setType(movie.getType());
        
        HashSet<String> genres = new HashSet<>();
        for (Genre g : movie.getGenres()) {
            genres.add(g.getGenre());
        }
        movieDTO.setGenres(genres);
        
        return movieDTO;
    }

    public List<MovieDTO> getMovieList(String search) throws Exception {
        EntityManager em = EMFSingleton.getEntityManagerFactory().createEntityManager();
        List<MovieDTO> result = new ArrayList<>();
        try {
            String jpql = "SELECT m FROM Movie m";
            if (search != null && !search.isEmpty()) {
                jpql += " WHERE LOWER(m.title) LIKE LOWER(:search)";
            }

            TypedQuery<Movie> query = em.createQuery(jpql, Movie.class);
            if (search != null && !search.isEmpty()) {
                query.setParameter("search", "%" + search + "%");
            }

            List<Movie> movies = query.getResultList();
            for (Movie m : movies) {
                // Mapping Entity -> DTO
                result.add(mapEntityToDTO(m));
            }
        } finally {
            em.close();
        }
        return result;
    }

    public void insertUpdateMovie(MovieDTO movieDTO) throws Exception {     
        EntityManager em = EMFSingleton.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            
            Movie movie;
            
            if(movieDTO.getId() != null) {
                movie = em.find(Movie.class, movieDTO.getId());
                if (movie == null) throw new Exception("Movie nicht gefunden");
                
                movie.getGenres().clear();

                movie.getCharacters().clear(); 
            } else {
                movie = new Movie();
            }

            movie.setTitle(movieDTO.getTitle());
            movie.setType(movieDTO.getType());
            movie.setYear(movieDTO.getYear());

            if (movieDTO.getGenres() != null) {
                for(String genreName : movieDTO.getGenres()) {
                    try {
                        TypedQuery<Genre> gq = em.createQuery("SELECT g FROM Genre g WHERE g.genre = :name", Genre.class);
                        gq.setParameter("name", genreName);
                        Genre genre = gq.getSingleResult();
                        
                        movie.getGenres().add(genre);
                    } catch (NoResultException e) {
                        throw new Exception("Genre existiert nicht: " + genreName);
                    }
                }
            }
            
            if (movieDTO.getCharacters() != null) {
                int position = 1;
                for(CharacterDTO charDTO : movieDTO.getCharacters()) {
                    // Person suchen
                    TypedQuery<Person> pq = em.createQuery("SELECT p FROM Person p WHERE p.name = :name", Person.class);
                    pq.setParameter("name", charDTO.getPlayer());
                    
                    Person person;
                    try {
                        person = pq.getSingleResult();
                    } catch (NoResultException e) {
                        throw new Exception("Schauspieler existiert nicht: " + charDTO.getPlayer());
                    }

                    MovieCharacter mc = new MovieCharacter();
                    mc.setMovie(movie);
                    mc.setPerson(person);
                    mc.setAlias(charDTO.getAlias());
                    mc.setCharacter(charDTO.getCharacter());
                    mc.setPosition(position++);

                    movie.getCharacters().add(mc);
                    
                    System.out.println("Create Character link: " + charDTO.getCharacter());
                }
            }

            if (movieDTO.getId() == null) {
                em.persist(movie);
            }
            
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void deleteMovie(long movieId) throws Exception {
        EntityManager em = EMFSingleton.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try { 
            tx.begin();
            Movie movie = em.find(Movie.class, movieId);
            if (movie == null) {
                throw new Exception("movie existiert nicht");
            }

            em.remove(movie);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }


    public MovieDTO getMovie(Long movieId) throws Exception {
        EntityManager em = EMFSingleton.getEntityManagerFactory().createEntityManager();
        try { 
            Movie movie = em.find(Movie.class, movieId);
            if(movie == null) { return null; }
            
            MovieDTO movieDTO = mapEntityToDTO(movie);
            for (MovieCharacter mc : movie.getCharacters()) {
                CharacterDTO characterDTO = new CharacterDTO();
                characterDTO.setAlias(mc.getAlias());
                characterDTO.setCharacter(mc.getCharacter()); 
                characterDTO.setPlayer(mc.getPerson().getName());
                
                movieDTO.addCharacter(characterDTO);
                System.out.println("found Character: " + characterDTO.getCharacter());
            }

            return movieDTO;
        } finally {
            em.close();
        }
    }
}