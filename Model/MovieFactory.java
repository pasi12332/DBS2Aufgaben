package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Service.DBConnection;

/**
 * Factory-Klasse für Movie-Entitäten.
 * Bietet Suchfunktionen zum Abrufen von Filmen aus der Datenbank.
 */
public class MovieFactory {
    
    /**
     * Findet einen Film anhand seiner ID
     * @param id Die ID des Films
     * @return Ein Movie-Objekt oder null, wenn kein Film gefunden wurde
     * @throws SQLException
     */
    public static Movie findById(long id) throws SQLException {
        String sql = "SELECT movieid, title, year, type FROM movie WHERE movieid = ?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Movie movie = new Movie();
                movie.movieID = rs.getLong("movieid");
                movie.title = rs.getString("title");
                movie.year = rs.getInt("year");
                movie.type = rs.getString("type");
                return movie;
            }
        }
        return null;
    }
    
    /**
     * Findet alle Filme mit einem bestimmten Titel (Wildcard-Suche)
     * @param title Der Titel (oder Teilstring des Titels) des Films
     * @return Eine Liste von Movie-Objekten
     * @throws SQLException
     */
    public static List<Movie> findByTitle(String title) throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT movieid, title, year, type FROM movie WHERE title ILIKE ?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + title + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Movie movie = new Movie();
                movie.movieID = rs.getLong("movieid");
                movie.title = rs.getString("title");
                movie.year = rs.getInt("year");
                movie.type = rs.getString("type");
                movies.add(movie);
            }
        }
        return movies;
    }
}
