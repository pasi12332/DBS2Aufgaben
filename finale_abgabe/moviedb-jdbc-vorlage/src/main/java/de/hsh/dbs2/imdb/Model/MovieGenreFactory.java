package de.hsh.dbs2.imdb.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class MovieGenreFactory {
    /**
     * Lädt ein {@link MovieGenre}-Objekt aus dem aktuellen Datensatz eines ResultSet.
     *
     * Diese Methode liest die relevanten Spalten aus dem übergebenen {@code ResultSet}
     * und erzeugt daraus ein neues {@link MovieGenre}-Objekt. Es wird vorausgesetzt,
     * dass sich der Cursor des ResultSet bereits auf einem gültigen Datensatz befindet.
     *
     * @param rs Das ResultSet, aus dem die Daten geladen werden sollen.
     * @return Ein aus dem ResultSet erzeugtes {@link MovieGenre}-Objekt.
     * @throws Exception Wenn beim Auslesen der Daten ein Fehler auftritt.
     */
    private static MovieGenre loadMovieGenre(ResultSet rs) throws Exception {
        MovieGenre movieGenre = new MovieGenre(rs.getLong("genreid"));
        movieGenre.setGenreId(rs.getLong("genreid"));
        movieGenre.setMovieId(rs.getLong("movieid"));
        return movieGenre;
    }

    /**
     * Findet alle Genre-Zuordnungen für einen bestimmten Film.
     *
     * Diese Methode führt eine SQL-Abfrage auf der Tabelle {@code moviegenre} aus,
     * die alle Datensätze ermittelt, deren {@code movieid} der übergebenen ID entspricht.
     * Jeder gefundene Eintrag wird mittels {@link #loadMovieGenre(ResultSet)} in ein
     * {@link MovieGenre}-Objekt umgewandelt und der Ergebnisliste hinzugefügt.
     *
     * @param movieID Die ID des Films, dessen Genre-Zuordnungen abgefragt werden sollen.
     * @param conn    Eine gültige Datenbankverbindung zum Ausführen der Abfrage.
     * @return Eine Liste von {@link MovieGenre}-Objekten, die dem Film zugeordnet sind.
     * @throws Exception Wenn ein Datenbankfehler auftritt.
     */
    public static List<MovieGenre> findByMovie(long movieID, Connection conn) throws Exception {
        List<MovieGenre> movieGenres = new ArrayList<>();
        String sql = "SELECT * FROM moviegenre WHERE movieid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, movieID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                movieGenres.add(loadMovieGenre(rs));
            }
            return movieGenres;
        }
    }
}