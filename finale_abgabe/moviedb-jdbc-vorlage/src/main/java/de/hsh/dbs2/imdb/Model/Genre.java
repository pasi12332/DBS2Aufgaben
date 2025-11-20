package de.hsh.dbs2.imdb.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * ActiveRecord-Klasse für Genre-Entität.
 * Repräsentiert ein Film-Genre in der Datenbank.
 */
public class Genre {
    private Long genreID;
    private String genre;


    public Genre() {}
    public Genre(Long genreID) { this.genreID = genreID; }

    /**
     * Fügt ein neues Genre in die Datenbank ein.
     * Die generierte ID wird in genreID gespeichert.
     * 
     * @throws SQLException wenn ein Datenbankfehler auftritt
     */
    public void insert(Connection conn) throws SQLException {
        String sql = "INSERT INTO genre (genre) Values (?)";
        try(PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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

    /**
     * Setzt den Genre-Namen.
     * 
     * @param genre der Name des Genres
     */
    public void setGenre(String genre) { this.genre = genre; }
    
    /**
     * Gibt die Genre-ID zurück.
     * 
     * @return die Genre-ID
     */
    public Long getGenreId() { return this.genreID; }
    
    /**
     * Gibt den Genre-Namen zurück.
     * 
     * @return der Genre-Name
     */
    public String getGenre() { return this.genre; }
}
