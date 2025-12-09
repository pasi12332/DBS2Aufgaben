package de.hsh.inform.dbs2;

import java.util.List;

import de.hsh.inform.dbs2.entities.Genre;
import de.hsh.inform.dbs2.entities.Movie;
import de.hsh.inform.dbs2.entities.MovieCharacter;
import de.hsh.inform.dbs2.entities.Person;
import de.hsh.inform.dbs2.util.EMFSingleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class Main {

    public static void main(String[] args) {
        // 1. Daten erstellen (Personen, Genres, Film, Charaktere)
        createStarWarsData();

        // 2. Daten kontrollieren (Abfrage über JPA)
        System.out.println("--------------------------------------------------");
        printMovieDetails("Star Wars");
        System.out.println("--------------------------------------------------");
        
        // Optional: Singleton/Factory schließen am Programmende, falls nötig
        // EMFSingleton.getEntityManagerFactory().close();
    }

    public static void createStarWarsData() {
        EntityManager em = EMFSingleton.getEntityManagerFactory().createEntityManager();
        EntityTransaction trx = em.getTransaction();

        try {
            trx.begin();

            Genre sciFi = new Genre("Sci-Fi");
            Genre action = new Genre("Action");
            em.persist(sciFi);
            em.persist(action);

            Person harrison = new Person("Harrison Ford");
            Person mark = new Person("Mark Hamill");
            Person carrie = new Person("Carrie Fisher");
            em.persist(harrison);
            em.persist(mark);
            em.persist(carrie);

            Movie movie = new Movie("Star Wars", "C", 1977);
            
            movie.addGenre(sciFi);
            movie.addGenre(action);
            
            em.persist(movie);

            
            MovieCharacter hanSolo = new MovieCharacter("Han Solo", "Captain", 1, movie, harrison);
            MovieCharacter luke = new MovieCharacter("Luke Skywalker", "Jedi", 1, movie, mark);
            MovieCharacter leia = new MovieCharacter("Leia Organa", "Princess", 1, movie, carrie);

            em.persist(hanSolo);
            em.persist(luke);
            em.persist(leia);

            trx.commit();
            System.out.println("Daten erfolgreich angelegt!");

        } catch (Exception ex) {
            if (trx.isActive()) {
                trx.rollback();
            }
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static void printMovieDetails(String movieTitle) {
        EntityManager em = EMFSingleton.getEntityManagerFactory().createEntityManager();
        try {
            List<Movie> results = em.createQuery(
                "SELECT m FROM Movie m WHERE m.title = :title", Movie.class)
                .setParameter("title", movieTitle)
                .getResultList();

            if (results.isEmpty()) {
                System.out.println("Kein Film gefunden mit Titel: " + movieTitle);
                return;
            }

            for (Movie m : results) {
                System.out.println("Film ID: " + m.getId());
                System.out.println("Titel:   " + m.getTitle() + " (" + m.getYear() + ")");
                
                System.out.print("Genres:  ");
                for (Genre g : m.getGenres()) {
                    System.out.print(g.getGenre() + " ");
                }
                System.out.println();

                System.out.println("Cast:");
                for (MovieCharacter mc : m.getCharacters()) {
                    System.out.println(" - " + mc.getCharacter() 
                                     + " (Alias: " + mc.getAlias() + ")"
                                     + " gespielt von " + mc.getPerson().getName());
                }
            }

        } finally {
            em.close();
        }
    }
}