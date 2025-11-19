package de.hsh.dbs2.imdb.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.hsh.dbs2.imdb.util.DBConnection;

/**
 * ActiveRecord-Klasse für MovieGenre-Entität.
 * Repräsentiert die N:M Zuordnung zwischen Filmen und Genres in der Datenbank.
 * Die Datenbank-Tabelle nutzt einen zusammengesetzten Primärschlüssel aus genreID und movieID.
 */
public class MovieGenre {
    Long movieGenreID;
    Long genreID;
    Long movieID;

    /**
     * Fügt eine neue Movie-Genre Zuordnung in die Datenbank ein.
     * Da der Primärschlüssel aus den Fremdschlüsseln besteht, ist keine ID-Generierung erforderlich.
     * 
     * @throws SQLException wenn ein Datenbankfehler auftritt
     */
    public void insert() throws SQLException {
        String sql = "INSERT INTO moviegenre (genreid, movieid) Values (?, ?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setLong(1, this.genreID);
                pstmt.setLong(2, this.movieID);

                int affectedRows = pstmt.executeUpdate();

                if(affectedRows == 0) {
                    throw new SQLException("Erstellen von MovieGenre fehlgeschlagen, keine Zeilen geändert.");
                }
        }
    }

    /**
     * Setzt die Genre-ID.
     * 
     * @param genreID die ID des Genres
     */
    public void setGenreId(Long genreID) { this.genreID = genreID; }
    
    /**
     * Setzt die Film-ID.
     * 
     * @param movieID die ID des Films
     */
    public void setMovieId(Long movieID) { this.movieID = movieID; }
    
    /**
     * Gibt die Genre-ID zurück.
     * 
     * @return die Genre-ID
     */
    public Long getGenreId() { return this.genreID; }
    
    /**
     * Gibt die Film-ID zurück.
     * 
     * @return die Film-ID
     */
    public Long getMovieId() { return this.movieID; }
}
