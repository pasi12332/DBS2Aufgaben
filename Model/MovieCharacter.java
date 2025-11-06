package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Service.DBConnection;

public class MovieCharacter {
    Long movCharID;
    Long movieID;
    Long personID;
    String character;
    String alias;
    int position;
    
    public void insert() throws SQLException {
        String sql = "INSERT INTO moviecharacter (movieID, personID, character, alias, position) Values (?, ?, ?, ?, ?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setLong(1, this.movieID);
                pstmt.setLong(2, personID);
                pstmt.setString(3, character);
                pstmt.setString(4, alias);
                pstmt.setInt(5, position);

                int affectedRows = pstmt.executeUpdate();

                if(affectedRows == 0) {
                    throw new SQLException("Erstellen von MovieCharacter fehlgeschlagen, keine Zeilen geändert.");
                }

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.movCharID = generatedKeys.getLong(1);
                    } else {
                        throw new SQLException("Erstellen von MovieCharacter fehlgeschlagen, keine ID erhalten.");
                    }
                }
        }
    }

    public void setMovieId(Long movieID) { this.movieID = movieID; }
    public void setPlayerId(Long personID) { this.personID = personID; }
    public void setCharacter(String character) { this.character = character; }
    public void setAlias(String alias) { this.alias = alias; }
    public void setPosition(int position) { this.position = position; }
}
