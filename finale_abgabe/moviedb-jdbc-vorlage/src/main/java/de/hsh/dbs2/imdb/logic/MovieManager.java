package de.hsh.dbs2.imdb.logic;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import de.hsh.dbs2.imdb.logic.dto.CharacterDTO;
import de.hsh.dbs2.imdb.logic.dto.MovieDTO;

import de.hsh.dbs2.imdb.util.DBConnection;
import de.hsh.dbs2.imdb.Model.Genre;
import de.hsh.dbs2.imdb.Model.GenreFactory;
import de.hsh.dbs2.imdb.Model.Movie;
import de.hsh.dbs2.imdb.Model.MovieCharacter;
import de.hsh.dbs2.imdb.Model.MovieFactory;
import de.hsh.dbs2.imdb.Model.MovieGenre;
import de.hsh.dbs2.imdb.Model.MovieGenreFactory;
import de.hsh.dbs2.imdb.Model.Person;
import de.hsh.dbs2.imdb.Model.PersonFactory;

public class MovieManager {

	public MovieDTO loadMovieDTO(Movie movie) throws Exception {
		MovieDTO movieDTO = new MovieDTO();
		GenreManager genreManager = new GenreManager();
		movieDTO.setId(movie.getMovieId());
		movieDTO.setTitle(movie.getTitle());
		movieDTO.setYear(movie.getYear());
		movieDTO.setType(movie.getType());
		movieDTO.setGenres(genreManager.getGenresByMovie(movie.getMovieId()));
		return movieDTO;
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
		List<MovieDTO> movieListDOT = new ArrayList<>();

		if (search.equals(null) || search.equals("")) {
			for (Movie movie : MovieFactory.getAll()) {
				movieListDOT.add(loadMovieDTO(movie));
			}
		} else {
			for (Movie movie : MovieFactory.findByTitle(search)) {
				movieListDOT.add(loadMovieDTO(movie));
			}	
		}
		
		return movieListDOT;
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
		Movie movie = MovieFactory.findById(movieDTO.getId());
		if(movie != null) {
			movie.setTitle(movieDTO.getTitle());
			movie.setType(movieDTO.getType());
			movie.setYear(movieDTO.getYear());
			for(MovieGenre movieGenre : MovieGenreFactory.findByMovie(movieDTO.getId())) {
				movieGenre.delete();
			}
			MovieFactory.deleteCharactersByMovieId(movieDTO.getId());
			for(CharacterDTO character : movieDTO.getCharacters()) {
				long personid = PersonFactory.findByName(character.getPlayer());
				
			}
			for(String genreS : movieDTO.getGenres()) {
				Genre genre = GenreFactory.findeByGenre(genreS);
				if(genre.equals(null)) {
					// abbruch
				} else {
					MovieGenre movieGenre = new MovieGenre();
					movieGenre.setGenreId(genre.getGenreId());
					movieGenre.setMovieId(movieDTO.getId());
					movieGenre.insert();
				}
			}

			movie.update();
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
