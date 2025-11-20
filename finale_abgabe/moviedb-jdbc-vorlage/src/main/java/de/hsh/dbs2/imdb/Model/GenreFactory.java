package de.hsh.dbs2.imdb.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import de.hsh.dbs2.imdb.util.DBConnection;

public class GenreFactory {

    private static Genre loadGenre(ResultSet rs) throws Exception {
        Genre genre = new Genre(rs.getLong("genreid"));
        genre.setGenre(rs.getString("genre"));
        return genre;
    }
    

    public static List<Genre> findByMovie(long movieID, Connection conn) throws Exception {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT * FROM MovieGenre WHERE movieid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, movieID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                genres.add(findeByID(rs.getLong("genreid"), conn));
            }
            
        } catch (Exception e) {
            System.out.println("error: " + e);
        }
        return genres;
    }


    public static Genre findeByID(long genreID, Connection conn) throws Exception {
        String sql = "SELECT * FROM genre WHERE genreid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, genreID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
               return loadGenre(rs); 
            }
            return null;
        }
    }

    public static Genre findeByGenre(String genreS, Connection conn) throws Exception {
        String sql = "SELECT * FROM genre WHERE genre = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, genreS);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return loadGenre(rs);
            }
            return null;
        }
    }

    public static List<Genre> getAll(Connection conn) throws Exception {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT * FROM genre ORDER BY genre ASC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                genres.add(loadGenre(rs));
            }
            return genres;
        }
        
    }

    


}
