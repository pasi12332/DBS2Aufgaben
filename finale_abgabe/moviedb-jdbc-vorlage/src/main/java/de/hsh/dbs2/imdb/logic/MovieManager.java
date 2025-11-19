package de.hsh.dbs2.imdb.logic;

import java.util.ArrayList;
import java.util.List;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import de.hsh.dbs2.imdb.logic.dto.MovieDTO;

import de.hsh.dbs2.imdb.util.DBConnection;
import de.hsh.dbs2.imdb.Model.Movie;

public class MovieManager {

	public MovieDTO loadMovie(ResultSet rs) throws Exception {
		MovieDTO movie = new MovieDTO();
		GenreManager genreManager = new GenreManager();
		movie.setId(rs.getLong("movieid"));
		movie.setTitle(rs.getString("title"));
		movie.setYear(rs.getInt("year"));
		movie.setType(rs.getString("type"));
		movie.setGenres(genreManager.getGenresByMovie(rs.getLong("movieid")));
		return movie;
	}

	/**
	 * Ermittelt alle Filme, deren Filmtitel den Suchstring enthaelt.
	 * Wenn der String leer ist, sollen alle Filme zurueckgegeben werden.
	 * Der Suchstring soll ohne Ruecksicht auf Gross-/Kleinschreibung verarbeitet werden.
	 * @param search Suchstring. 
	 * @return Liste aller passenden Filme als MovieDTO
	 * @throws Exception Beschreibt evtl. aufgetretenen Fehler
	 */
	public List<MovieDTO> getMovieList(String search) throws Exception {
		List<MovieDTO> movieList = new ArrayList<MovieDTO>();
		PreparedStatement pstmt;
		try (Connection conn = DBConnection.getConnection()) {
			if (search.equals(null) || search.equals("")) {
				String sql = "SELECT * FROM movie";
				pstmt  = conn.prepareStatement(sql);
			} else {
				String sql = "Selecte * FROM movie WHERE title ILIKE ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, search);
			}
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				movieList.add(loadMovie(rs));
			}
		}
		return movieList;
	}

	/**
	 * Speichert die uebergebene Version des Films neu in der Datenbank oder aktualisiert den
	 * existierenden Film.
	 * Dazu werden die Daten des Films selbst (Titel, Jahr, Typ) beruecksichtigt,
	 * aber auch alle Genres, die dem Film zugeordnet sind und die Liste der Charaktere
	 * auf den neuen Stand gebracht.
	 * @param movieDTO Film-Objekt mit Genres und Charakteren.
	 * @throws Exception Beschreibt evtl. aufgetretenen Fehler
	 */
	public void insertUpdateMovie(MovieDTO movieDTO) throws Exception {		
		String sql = "SELECT movieid FROM movie WHERE movieid = ?";
		PreparedStatement pstmt;
		ResultSet rs;
		Movie movie;

		try(Connection conn = DBConnection.getConnection()) {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, movieDTO.getId());
			rs =  pstmt.executeQuery();
			if(rs.next()) {
				movie = new Movie(movieDTO.getId());
				movie.setTitle(movieDTO.getTitle());
				movie.setType(movieDTO.getType());
				movie.setYear(movieDTO.getYear());
				movie.update();
			}
			else {
				movie = new Movie();
				movie.setTitle(movieDTO.getTitle());
				movie.setType(movieDTO.getType());
				movie.setYear(movieDTO.getYear());
				movie.insert();

				for (String genre : movieDTO.getGenres()) {
					sql = "SELECT genreid WHERE genre = ";
				}

			}
			
		}
	}

	/**
	 * Loescht einen Film aus der Datenbank. Es werden auch alle abhaengigen Objekte geloescht,
	 * d.h. alle Charaktere und alle Genre-Zuordnungen.
	 * @param movieId id des zu löschenden Films
	 * @throws Exception Beschreibt evtl. aufgetretenen Fehler
	 */
	public void deleteMovie(int movieId) throws Exception {
		/* TODO */
	}

	/**
	 * Ermittelt alle Daten zu einem Movie (d.h. auch Genres und Charaktere) und
	 * trägt diese Daten in einem MovieDTO-Objekt ein.
	 * @param movieId ID des Films der eingelesen wird.
	 * @return MovieDTO-Objekt mit allen Informationen zu dem Film
	 * @throws Exception Z.B. bei Datenbank-Fehlern oder falls der Movie nicht existiert.
	 */
	public MovieDTO getMovie(int movieId) throws Exception {
		/* TODO */
		return null;
	}
	
}
