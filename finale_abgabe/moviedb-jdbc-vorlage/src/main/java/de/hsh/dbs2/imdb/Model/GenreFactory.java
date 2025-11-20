package de.hsh.dbs2.imdb.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class GenreFactory {

    /**
     * Lädt ein {@link Genre}-Objekt aus dem aktuellen Datensatz eines ResultSet.
     *
     * Diese Methode liest die Spalten {@code genreid} und {@code genre} aus dem
     * übergebenen {@code ResultSet} und erzeugt daraus ein entsprechendes {@link Genre}-Objekt.
     * Es wird vorausgesetzt, dass der Cursor des ResultSet bereits auf einem gültigen
     * Datensatz positioniert ist.
     *
     * @param rs Das ResultSet, aus dem die Genredaten geladen werden sollen.
     * @return Ein {@link Genre}-Objekt, das aus dem aktuellen Datensatz erzeugt wurde.
     * @throws Exception Wenn ein Fehler beim Auslesen des ResultSet auftritt.
     */
    private static Genre loadGenre(ResultSet rs) throws Exception {
        Genre genre = new Genre(rs.getLong("genreid"));
        genre.setGenre(rs.getString("genre"));
        return genre;
    }
    

    /**
     * Findet alle Genres, die einem bestimmten Film zugeordnet sind.
     *
     * Diese Methode liest alle Datensätze aus der Tabelle {@code MovieGenre}, deren
     * {@code movieid} mit der übergebenen ID übereinstimmt. Für jeden Eintrag wird
     * das zugehörige Genre über {@link #findeByID(long, Connection)} geladen.
     *
     * @param movieID Die ID des Films, dessen Genres geladen werden sollen.
     * @param conn    Eine gültige Datenbankverbindung.
     * @return Eine Liste aller Genres, die dem Film zugeordnet sind.
     * @throws Exception Wenn ein Datenbankfehler beim Lesen der Zuordnungen auftritt.
     */
    public static List<Genre> findByMovie(long movieID, Connection conn) throws Exception {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT * FROM MovieGenre WHERE movieid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, movieID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                genres.add(findeByID(rs.getLong("genreid"), conn));
            }
            
        } catch (Exception e) {
            System.out.println("error: " + e);
        }
        return genres;
    }


    /**
     * Sucht ein Genre anhand seiner ID.
     *
     * Diese Methode liest aus der Tabelle {@code genre} den Datensatz aus,
     * dessen {@code genreid} der übergebenen ID entspricht. Wird ein Treffer gefunden,
     * wird dieser als {@link Genre}-Objekt zurückgegeben, sonst {@code null}.
     *
     * @param genreID Die ID des gesuchten Genres.
     * @param conn    Eine gültige Datenbankverbindung.
     * @return Das gefundene {@link Genre} oder {@code null}, wenn kein Eintrag existiert.
     * @throws Exception Wenn ein Fehler beim Zugriff auf die Datenbank auftritt.
     */
    public static Genre findeByID(long genreID, Connection conn) throws Exception {
        String sql = "SELECT * FROM genre WHERE genreid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, genreID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
               return loadGenre(rs); 
            }
            return null;
        }
    }


    /**
     * Sucht ein Genre anhand seines Namens.
     *
     * Diese Methode durchsucht die Tabelle {@code genre} nach einem Eintrag,
     * dessen {@code genre}-Spalte dem übergebenen String entspricht. Wird ein
     * entsprechender Datensatz gefunden, wird er als {@link Genre}-Objekt zurückgegeben.
     *
     * @param genreS Der Genre-Name, nach dem gesucht werden soll.
     * @param conn   Eine gültige Datenbankverbindung.
     * @return Das gefundene {@link Genre} oder {@code null}, falls kein Eintrag existiert.
     * @throws Exception Wenn ein Fehler beim Ausführen der SQL-Abfrage auftritt.
     */
    public static Genre findeByGenre(String genreS, Connection conn) throws Exception {
        String sql = "SELECT * FROM genre WHERE genre = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, genreS);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return loadGenre(rs);
            }
            return null;
        }
    }


    /**
     * Lädt alle Genres aus der Datenbank.
     *
     * Diese Methode führt eine Abfrage auf der Tabelle {@code genre} aus und gibt alle
     * Einträge in alphabetischer Reihenfolge zurück. Jeder Datensatz wird mittels
     * {@link #loadGenre(ResultSet)} in ein {@link Genre}-Objekt umgewandelt.
     *
     * @param conn Eine gültige Datenbankverbindung.
     * @return Eine alphabetisch sortierte Liste aller vorhandenen Genres.
     * @throws Exception Wenn ein Fehler beim Ausführen der Datenbankabfrage auftritt.
     */
    public static List<Genre> getAll(Connection conn) throws Exception {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT * FROM genre ORDER BY genre ASC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                genres.add(loadGenre(rs));
            }
            return genres;
        }
        
    }

    


}
