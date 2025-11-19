package de.hsh.dbs2.imdb.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.hsh.dbs2.imdb.logic.dto.MovieDTO;
import de.hsh.dbs2.imdb.util.DBConnection;

/**
 * ActiveRecord-Klasse für Movie-Entität.
 * Repräsentiert einen Film in der Datenbank.
 * Bietet Methoden zum Einfügen, Aktualisieren und Löschen.
 */
public class Movie {
    private Long movieID;
    private String title;
    private int year;
    private String type;


    public Movie(Long movieID) {
        this.movieID = movieID;
    }

    public Movie(){}


    /**
     * Fügt einen neuen Film in die Datenbank ein.
     * Die generierte ID wird in movieID gespeichert.
     * 
     * @throws SQLException wenn ein Datenbankfehler auftritt
     */
    public void insert() throws SQLException {
        String sql = "INSERT INTO movie (title, year, type) Values (?, ?, ?)";
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

    /**
     * Aktualisiert einen bestehenden Film in der Datenbank.
     * Der Film wird anhand seiner ID identifiziert.
     * 
     * @throws SQLException wenn ein Datenbankfehler auftritt
     */
    public void update() throws SQLException {
        String sql = "UPDATE movie SET title = ?, year = ?, type = ? WHERE movieid = ?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, this.title);
                pstmt.setInt(2, this.year);
                pstmt.setString(3, this.type);
                pstmt.setLong(4, this.movieID);

                int affectedRows = pstmt.executeUpdate();
                if(affectedRows == 0) {
                    throw new SQLException("Aktualisieren von Movie fehlgeschlagen, keine Zeilen geändert.");
                }
        }
    }

    /**
     * Löscht einen Film aus der Datenbank.
     * Der Film wird anhand seiner ID identifiziert.
     * 
     * @throws SQLException wenn ein Datenbankfehler auftritt
     */
    public void delete() throws SQLException {
        String sql = "DELETE FROM movie WHERE movieid = ?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setLong(1, this.movieID);

                int affectedRows = pstmt.executeUpdate();
                if(affectedRows == 0) {
                    throw new SQLException("Löschen von Movie fehlgeschlagen, keine Zeilen geändert.");
                }
        }
    }


    /**
     * Setzt den Filmtitel.
     * 
     * @param title der Titel des Films
     */
    public void setTitle(String title){ this.title = title; }

    /**
     * Setzt das Erscheinungsjahr des Films.
     * 
     * @param year das Jahr
     */
    public void setYear(int year){ this.year = year; }

    /**
     * Setzt den Filmtyp.
     * 
     * @param type der Typ des Films
     */
    public void setType(String type){ this.type = type; }

    /**
     * Gibt die Film-ID zurück.
     * 
     * @return die Film-ID
     */
    public Long getMovieId() { return this.movieID; }
    
    /**
     * Gibt den Filmtitel zurück.
     * 
     * @return der Titel des Films
     */
    public String getTitle() { return this.title; }
    
    /**
     * Gibt das Erscheinungsjahr zurück.
     * 
     * @return das Jahr
     */
    public int getYear() { return this.year; }
    
    /**
     * Gibt den Filmtyp zurück.
     * 
     * @return der Typ des Films
     */
    public String getType() { return this.type; }
}
