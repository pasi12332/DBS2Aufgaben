package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Service.DBConnection;

public class Movie {
    Long movieID;
    String title;
    int year;
    String type;


    public void insert() throws SQLException {
        String sql = "INSERT INTO person (title, year, type) Values (?, ?, ?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, this.title);
                pstmt.setInt(2, this.year);
                pstmt.setString(3, this.type);

                int affectedRows = pstmt.executeUpdate();

                if(affectedRows == 0) {
                    throw new SQLException("Erstellen von Movie fehlgeschlagen, keine Zeilen geändert.");
                }

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.movieID = generatedKeys.getLong(1);
                    } else {
                        throw new SQLException("Erstellen von Movie fehlgeschlagen, keine ID erhalten.");
                    }
                }
        }
    }

    public void setTitle(String title){ this.title = title; }

    public void setYear(int year){ this.year = year; }

    public void setType(String type){ this.type = type; }

    public Long getMovieID() { return this.movieID; }


}
