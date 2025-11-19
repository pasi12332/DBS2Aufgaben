package de.hsh.dbs2.imdb.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.hsh.dbs2.imdb.util.DBConnection;

/**
 * ActiveRecord-Klasse für Person-Entität.
 * Repräsentiert eine Person (Schauspieler/in) in der Datenbank.
 */
public class Person {
    Long personID;
    String name;

    /**
     * Fügt eine neue Person in die Datenbank ein.
     * Die genierte ID wird in personID gespeichert.
     * 
     * @throws SQLException wenn ein Datenbankfehler auftritt
     */
    public void insert() throws SQLException {
        String sql = "INSERT INTO person (name) Values (?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, this.name);

                int affectedRows = pstmt.executeUpdate();

                if(affectedRows == 0) {
                    throw new SQLException("Erstellen von Person fehlgeschlagen, keine Zeilen geändert.");
                }

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.personID = generatedKeys.getLong(1);
                    } else {
                        throw new SQLException("Erstellen von Person fehlgeschlagen, keine ID erhalten.");
                    }
                }
        }
    }
    
    
    /**
     * Setzt den Namen der Person.
     * 
     * @param name der Name der Person
     */
    public void setName(String name) { this.name = name; }

    /**
     * Gibt die ID der Person zurück.
     * 
     * @return die Person-ID
     */
    public Long getPersonId(){ return this.personID; }
    
    /**
     * Gibt den Namen der Person zurück.
     * 
     * @return der Name der Person
     */
    public String getName() { return this.name; }
}
