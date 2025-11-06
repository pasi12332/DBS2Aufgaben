package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import Service.DBConnection;

public class Genre {
    private Long genreID;
    private String genre;

    public void insert() throws SQLException {
        String sql = "INSERT INTO genre (genre) Values (?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, genre);

                int affectedRows = pstmt.executeUpdate();

                if(affectedRows == 0) {
                    throw new SQLException("Erstellen von Genre fehlgeschlagen, keine Zeilen geändert.");
                }

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.genreID = generatedKeys.getLong(1);
                    } else {
                        throw new SQLException("Erstellen von Genre fehlgeschlagen, keine ID erhalten.");
                    }
                }
        }
    }

    public void setGenre(String genre) { this.genre = genre; }
}
