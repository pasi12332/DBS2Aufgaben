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
            }
            return chracters;
        }
    }
}
