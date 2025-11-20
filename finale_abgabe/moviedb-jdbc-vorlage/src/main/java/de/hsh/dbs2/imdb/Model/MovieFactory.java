package de.hsh.dbs2.imdb.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Factory-Klasse für Movie-Entitäten.
 * Bietet Suchfunktionen zum Abrufen von Filmen aus der Datenbank.
 */
public class MovieFactory {
    

    /**
     * Lädt ein {@link Movie}-Objekt aus dem aktuellen Datensatz eines ResultSet.
     *
     * Diese Methode liest die Spalten {@code movieid}, {@code title}, {@code year}
     * und {@code type} aus dem übergebenen {@code ResultSet} und erzeugt daraus ein
     * vollständig initialisiertes {@link Movie}-Objekt. Es wird vorausgesetzt, dass
     * sich der Cursor des ResultSet bereits auf einem gültigen Datensatz befindet.
     *
     * @param rs Das ResultSet, aus dem die Filmdaten geladen werden sollen.
     * @return Ein {@link Movie}-Objekt, das aus dem aktuellen Datensatz erzeugt wurde.
     * @throws Exception Wenn beim Auslesen der Daten ein Fehler auftritt.
     */
    private static Movie loadMovie(ResultSet rs) throws Exception {
        Movie movie = new Movie(rs.getLong("movieid"));
        movie.setTitle(rs.getString("title"));
        movie.setYear(rs.getInt("year"));
        movie.setType(rs.getString("type"));
        return movie;
    }


    /**
     * Findet einen Film anhand seiner ID
     * @param id Die ID des Films
     * @return Ein Movie-Objekt oder null, wenn kein Film gefunden wurde
     * @throws SQLException
     */
    public static Movie findById(long id, Connection conn) throws Exception {
        String sql = "SELECT * FROM movie WHERE movieid = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return loadMovie(rs);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }


    /**
     * Ruft alle Filme aus der Tabelle {@code movie} ab.
     *
     * Diese Methode führt eine SELECT-Abfrage ohne Filter aus und lädt jeden
     * gefundenen Datensatz über {@link #loadMovie(ResultSet)} in ein {@link Movie}-Objekt.
     * Alle geladenen Filme werden in einer Liste gesammelt und zurückgegeben.
     *
     * @param conn Eine gültige Datenbankverbindung.
     * @return Eine Liste aller in der Datenbank vorhandenen {@link Movie}-Objekte.
     * @throws Exception Wenn ein Fehler beim Ausführen der Datenbankabfrage auftritt.
     */
    public static List<Movie> getAll(Connection conn) throws Exception {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movie";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                movies.add(loadMovie(rs));
            }
            return movies;
        }
        
    }
    
    /**
     * Findet alle Filme mit einem bestimmten Titel (Wildcard-Suche)
     * @param title Der Titel (oder Teilstring des Titels) des Films
     * @return Eine Liste von Movie-Objekten
     * @throws SQLException
     */
    public static List<Movie> findByTitle(String title, Connection conn) throws Exception {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT movieid, title, year, type FROM movie WHERE title ILIKE ?"; // ILIKE ignoriert klein/großschr. im gegensatz zu LIKE
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + title + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                movies.add(loadMovie(rs));
            }
        }
        return movies;
    }


    public static void deleteCharactersByMovieId(long movieId, Connection conn) throws Exception {
        String sql = "DELETE FROM moviecharacter WHERE movieID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, movieId);
            pstmt.executeUpdate();
        }
    }


    public static List<MovieCharacter> getCharacterByMovieId(long movieid, Connection conn) throws Exception {
        List<MovieCharacter> chracters = new ArrayList<>();
        String sql = "SELECT * FROM MovieCharacter WHERE movieid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, movieid);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                MovieCharacter movieCharacter = new MovieCharacter(rs.getLong("movcharID"));
                movieCharacter.setAlias(rs.getString("alias"));
                movieCharacter.setCharacter(rs.getString("character"));
                movieCharacter.setPosition(rs.getInt("position"));
                movieCharacter.setMovieId(movieid);
                movieCharacter.setPlayerId(rs.getLong("personID"));
                chracters.add(movieCharacter);
            }
            return chracters;
        }
    }
}
