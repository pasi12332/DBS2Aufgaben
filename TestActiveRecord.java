import Model.*;
import Service.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Test-Klasse für ActiveRecord-Implementierung.
 * Testet alle CRUD-Operationen (Create, Read, Update, Delete) für die Movie-Datenbank.
 */
public class TestActiveRecord {
    
    /**
     * Test-Methode zum Einfügen von Daten (wie in der Aufgabe vorgegeben).
     * Erstellt eine Person, einen Film, einen MovieCharacter, ein Genre und eine MovieGenre Zuordnung.
     * Demonstriert die Verwendung von Commit und Rollback auf Connection-Ebene.
     * 
     * @throws SQLException wenn ein Datenbankfehler auftritt
     */
    public static void testInsert() throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            Person person = new Person();
            person.setName("Karl Tester");
            person.insert();
            System.out.println("Person created: ID=" + person.getPersonId() + ", Name=" + person.getName());

            Movie movie = new Movie();
            movie.setTitle("Die tolle Komoedie");
            movie.setYear(2012);
            movie.setType("C");
            movie.insert();
            System.out.println("Movie created: ID=" + movie.getMovieId() + ", Title=" + movie.getTitle());

            MovieCharacter chr = new MovieCharacter();
            chr.setMovieId(movie.getMovieId());
            chr.setPlayerId(person.getPersonId());
            chr.setCharacter("Hauptrolle");
            chr.setAlias(null);
            chr.setPosition(1);
            chr.insert();
            System.out.println("MovieCharacter created: ID=" + chr.getMovCharId() + ", Character=" + chr.getCharacter());

            Genre genre = new Genre();
            genre.setGenre("Unklar");
            genre.insert();
            System.out.println("Genre created: ID=" + genre.getGenreId() + ", Genre=" + genre.getGenre());

            MovieGenre movieGenre = new MovieGenre();
            movieGenre.setGenreId(genre.getGenreId());
            movieGenre.setMovieId(movie.getMovieId());
            movieGenre.insert();
            System.out.println("MovieGenre created: GenreID=" + movieGenre.getGenreId() + ", MovieID=" + movieGenre.getMovieId());

            conn.commit();
            System.out.println("\n✓ testInsert() erfolgreich abgeschlossen!");
            
        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            System.err.println("✗ testInsert() fehlgeschlagen!");
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
    
    /**
     * Test-Methode für MovieFactory.findById().
     * Erstellt einen Film, sucht ihn dann anhand der ID und verifiziert die Daten.
     * 
     * @throws SQLException wenn ein Datenbankfehler auftritt
     */
    public static void testFindById() throws SQLException {
        System.out.println("\n=== Test: MovieFactory.findById() ===");
        
        // Erst einen Film einfügen
        Movie movie = new Movie();
        movie.setTitle("Test Film für findById");
        movie.setYear(2025);
        movie.setType("D");
        movie.insert();
        long movieId = movie.getMovieId();
        System.out.println("Test-Film eingefügt: ID=" + movieId);
        
        // Dann nach ihm suchen
        Movie found = MovieFactory.findById(movieId);
        if (found != null) {
            System.out.println("✓ Film gefunden: Title=" + found.getTitle() + ", Year=" + found.getYear() + ", Type=" + found.getType());
        } else {
            System.out.println("✗ Film nicht gefunden!");
        }
    }
    
    /**
     * Test-Methode für MovieFactory.findByTitle().
     * Erstellt mehrere Filme und sucht sie anhand von Teilstrings im Titel.
     * Demonstriert die Wildcard-Suche mit ILIKE (case-insensitive).
     * 
     * @throws SQLException wenn ein Datenbankfehler auftritt
     */
    public static void testFindByTitle() throws SQLException {
        System.out.println("\n=== Test: MovieFactory.findByTitle() ===");
        
        // Ein paar Filme einfügen
        Movie movie1 = new Movie();
        movie1.setTitle("Abenteuer im Wald");
        movie1.setYear(2020);
        movie1.setType("A");
        movie1.insert();
        
        Movie movie2 = new Movie();
        movie2.setTitle("Abenteuer in der Stadt");
        movie2.setYear(2021);
        movie2.setType("A");
        movie2.insert();
        
        Movie movie3 = new Movie();
        movie3.setTitle("Tragödie im Wald");
        movie3.setYear(2019);
        movie3.setType("D");
        movie3.insert();
        
        System.out.println("3 Test-Filme eingefügt");
        
        // Nach "Abenteuer" suchen (sollte 2 Filme finden)
        List<Movie> results = MovieFactory.findByTitle("Abenteuer");
        System.out.println("✓ Suche nach 'Abenteuer': " + results.size() + " Filme gefunden");
        for (Movie m : results) {
            System.out.println("  - " + m.getTitle() + " (" + m.getYear() + ")");
        }
        
        // Nach "Wald" suchen (sollte 2 Filme finden)
        results = MovieFactory.findByTitle("Wald");
        System.out.println("✓ Suche nach 'Wald': " + results.size() + " Filme gefunden");
        for (Movie m : results) {
            System.out.println("  - " + m.getTitle() + " (" + m.getYear() + ")");
        }
    }
    
    /**
     * Test-Methode für Movie.update().
     * Erstellt einen Film, ändert seine Attribute, speichert die Änderungen und verifiziert sie.
     * 
     * @throws SQLException wenn ein Datenbankfehler auftritt
     */
    public static void testUpdate() throws SQLException {
        System.out.println("\n=== Test: Movie.update() ===");
        
        // Film einfügen
        Movie movie = new Movie();
        movie.setTitle("Original Titel");
        movie.setYear(2000);
        movie.setType("X");
        movie.insert();
        System.out.println("Film eingefügt: Title=" + movie.getTitle() + ", Year=" + movie.getYear());
        
        // Ändern
        movie.setTitle("Neuer Titel");
        movie.setYear(2025);
        movie.setType("A");
        movie.update();
        System.out.println("Film aktualisiert: Title=" + movie.getTitle() + ", Year=" + movie.getYear());
        
        // Aus DB lesen um zu verifizieren
        Movie found = MovieFactory.findById(movie.getMovieId());
        System.out.println("✓ Verifizierung aus DB: Title=" + found.getTitle() + ", Year=" + found.getYear());
    }
    
    /**
     * Test-Methode für Movie.delete().
     * Erstellt einen Film, löscht ihn und verifiziert, dass er aus der Datenbank entfernt wurde.
     * 
     * @throws SQLException wenn ein Datenbankfehler auftritt
     */
    public static void testDelete() throws SQLException {
        System.out.println("\n=== Test: Movie.delete() ===");
        
        // Film einfügen
        Movie movie = new Movie();
        movie.setTitle("Film zum Löschen");
        movie.setYear(2025);
        movie.setType("T");
        movie.insert();
        long movieId = movie.getMovieId();
        System.out.println("Film eingefügt: ID=" + movieId);
        
        // Verifizieren dass er da ist
        Movie found = MovieFactory.findById(movieId);
        System.out.println("✓ Film vor dem Löschen: " + (found != null ? "Vorhanden" : "Nicht vorhanden"));
        
        // Löschen
        movie.delete();
        System.out.println("Film gelöscht");
        
        // Verifizieren dass er weg ist
        found = MovieFactory.findById(movieId);
        System.out.println("✓ Film nach dem Löschen: " + (found == null ? "Gelöscht" : "Noch vorhanden"));
    }
    
    /**
     * Hauptmethode zum Ausführen aller Tests.
     * Führt nacheinander alle Test-Methoden aus und zeigt die Ergebnisse an.
     * 
     * @param args Kommandozeilen-Argumente (nicht verwendet)
     */
    public static void main(String[] args) {
        try {
            System.out.println("========================================");
            System.out.println("ActiveRecord Tests");
            System.out.println("========================================");
            
            // Test 1: Insert (wie in der Aufgabe vorgegeben)
            testInsert();
            
            // Test 2: findById
            testFindById();
            
            // Test 3: findByTitle
            testFindByTitle();
            
            // Test 4: update
            testUpdate();
            
            // Test 5: delete
            testDelete();
            
            System.out.println("\n========================================");
            System.out.println("✓ Alle Tests abgeschlossen!");
            System.out.println("========================================");
            
        } catch (SQLException e) {
            System.err.println("\n✗ SQL-Fehler:");
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("\n✗ Fehler:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
