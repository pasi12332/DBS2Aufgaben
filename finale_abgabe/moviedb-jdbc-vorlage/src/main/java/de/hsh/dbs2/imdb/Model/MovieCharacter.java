package de.hsh.dbs2.imdb.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.hsh.dbs2.imdb.util.DBConnection;

/**
 * ActiveRecord-Klasse für MovieCharacter-Entität.
 * Repräsentiert die Zuordnung einer Person zu einer Rolle in einem Film.
 */
public class MovieCharacter {
    Long movCharID;
    Long movieID;
    Long personID;
    String character;
    String alias;
    int position;

    public MovieCharacter() { }
    public MovieCharacter(Long movCharID) { this.movCharID = movCharID; }
    
    /**
     * Fügt einen neuen Movie-Character in die Datenbank ein.
     * Die generierte ID wird in movCharID gespeichert.
     * 
     * @throws SQLException wenn ein Datenbankfehler auftritt
     */
    public void insert() throws SQLException {
        String sql = "INSERT INTO moviecharacter (movieid, personid, character, alias, position) Values (?, ?, ?, ?, ?)";
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

    /**
     * Setzt die Film-ID.
     * 
     * @param movieID die ID des Films
     */
    public void setMovieId(Long movieID) { this.movieID = movieID; }
    
    /**
     * Setzt die Person-ID (Schauspieler).
     * 
     * @param personID die ID der Person
     */
    public void setPlayerId(Long personID) { this.personID = personID; }
    
    /**
     * Setzt den Charakternamen.
     * 
     * @param character der Name des Charakters
     */
    public void setCharacter(String character) { this.character = character; }
    
    /**
     * Setzt den Alias des Charakters.
     * 
     * @param alias der Alias (Optional)
     */
    public void setAlias(String alias) { this.alias = alias; }
    
    /**
     * Setzt die Position/Reihenfolge des Charakters im Film.
     * 
     * @param position die Position
     */
    public void setPosition(int position) { this.position = position; }
    
    /**
     * Gibt die MovieCharacter-ID zurück.
     * 
     * @return die MovieCharacter-ID
     */
    public Long getMovCharId() { return this.movCharID; }
    
    /**
     * Gibt die Film-ID zurück.
     * 
     * @return die Film-ID
     */
    public Long getMovieId() { return this.movieID; }
    
    /**
     * Gibt die Person-ID zurück.
     * 
     * @return die Person-ID
     */
    public Long getPlayerId() { return this.personID; }
    
    /**
     * Gibt den Charakternamen zurück.
     * 
     * @return der Charaktername
     */
    public String getCharacter() { return this.character; }
    
    /**
     * Gibt den Alias des Charakters zurück.
     * 
     * @return der Alias
     */
    public String getAlias() { return this.alias; }
    
    /**
     * Gibt die Position des Charakters zurück.
     * 
     * @return die Position
     */
    public int getPosition() { return this.position; }
}
