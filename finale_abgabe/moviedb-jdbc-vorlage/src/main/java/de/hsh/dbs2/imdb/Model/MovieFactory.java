package de.hsh.dbs2.imdb.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.hsh.dbs2.imdb.util.DBConnection;

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
    public static Movie findById(long id) throws Exception {
        String sql = "SELECT * FROM movie WHERE movieid = ?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return loadMovie(rs);
            }
        }
        return null;
    }



    public static List<Movie> getAll() throws Exception {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movie";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
    public static List<Movie> findByTitle(String title) throws Exception {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT movieid, title, year, type FROM movie WHERE title ILIKE ?"; // ILIKE ignoriert klein/großschr. im gegensatz zu LIKE
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + title + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                movies.add(loadMovie(rs));
            }
        }
        return movies;
    }
    public void deleteCharactersByMovieId(long movieId)throws Exception {
        String sql = "DELETE FROM moviecharacter WHERE movieID = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, movieId);
            pstmt.executeUpdate();
        }
    }
}
