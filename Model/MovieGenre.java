package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Service.DBConnection;

public class MovieGenre {
    Long movieGenreID;
    Long genreID;
    Long movieID;


    public void insert() throws SQLException {
        String sql = "INSERT INTO person (genreID, movieID) Values (?, ?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setLong(1, this.genreID);
                pstmt.setLong(2, this.movieID);

                int affectedRows = pstmt.executeUpdate();

                if(affectedRows == 0) {
                    throw new SQLException("Erstellen von Movie fehlgeschlagen, keine Zeilen geändert.");
                }

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.movieGenreID = generatedKeys.getLong(1);
                    } else {
                        throw new SQLException("Erstellen von Movie fehlgeschlagen, keine ID erhalten.");
                    }
                }
        }
    }

    public void setGenreId(Long genreID) { this.genreID = genreID; }
    public void setMovieId(Long movieID) { this.movieID = movieID; }
}
