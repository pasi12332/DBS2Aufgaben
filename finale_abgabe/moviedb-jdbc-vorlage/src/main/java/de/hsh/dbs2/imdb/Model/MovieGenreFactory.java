package de.hsh.dbs2.imdb.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class MovieGenreFactory {
    private static MovieGenre loadMovieGenre(ResultSet rs) throws Exception {
        MovieGenre movieGenre = new MovieGenre(rs.getLong("genreid"));
        movieGenre.setGenreId(rs.getLong("genreid"));
        movieGenre.setMovieId(rs.getLong("movieid"));
        return movieGenre;
    }
    public static List<MovieGenre> findByMovie(long movieID, Connection conn) throws Exception {
        List<MovieGenre> movieGenres = new ArrayList<>();
        String sql = "SELECT * FROM moviegenre WHERE movieid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, movieID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                movieGenres.add(loadMovieGenre(rs));
            }
            return movieGenres;
        }
    }
}