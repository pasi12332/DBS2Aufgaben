package de.hsh.dbs2.imdb.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.sql.Connection;

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

	public void createCharacterMovie(Long personid, CharacterDTO character, Long movieid) throws Exception {
		MovieCharacter movieCharacter = new MovieCharacter();
		movieCharacter.setAlias(character.getAlias());
		movieCharacter.setCharacter(character.getPlayer());
		movieCharacter.setPlayerId(personid);
		movieCharacter.setPosition(0); // Keine Ahnung woher wir die Position bekommen
		movieCharacter.setMovieId(movieid);
		movieCharacter.insert();
	}


	public void createMovieGenre(long genreid, long movieid) throws Exception {
		MovieGenre movieGenre = new MovieGenre();
		movieGenre.setGenreId(genreid);
		movieGenre.setMovieId(movieid);
		movieGenre.insert();
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
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			Movie movie = MovieFactory.findById(movieDTO.getId());
			if(movie != null) {
				movie.setTitle(movieDTO.getTitle());
				movie.setType(movieDTO.getType());
				movie.setYear(movieDTO.getYear());
				for(MovieGenre movieGenre : MovieGenreFactory.findByMovie(movieDTO.getId())) {
					movieGenre.delete();
				}
				MovieFactory.deleteCharactersByMovieId(movieDTO.getId());
				movie.update();
			} else {
				movie = new Movie();
				movie.setTitle(movieDTO.getTitle());
				movie.setType(movieDTO.getType());
				movie.setYear(movieDTO.getYear());
				movie.insert();
			}
			for(CharacterDTO character : movieDTO.getCharacters()) {
				Long personid = PersonFactory.findByName(character.getPlayer());
				if(personid == null ) { 
					throw new Exception("Person exisistiert");
				}
				createCharacterMovie(personid, character, movieDTO.getId());
			}
			for(String genreS : movieDTO.getGenres()) {
				Genre genre = GenreFactory.findeByGenre(genreS);
				if(genre.equals(null)) {
					throw new Exception("Genre exisistiert");
				}
				createMovieGenre(genre.getGenreId(), movieDTO.getId());
			}
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
		}
		conn.close();
	}

	/**
	 * Loescht einen Film aus der Datenbank. Es werden auch alle abhaengigen Objekte geloescht,
	 * d.h. alle Charaktere und alle Genre-Zuordnungen.
	 * @param movieId id des zu löschenden Films
	 * @throws Exception Beschreibt evtl. aufgetretenen Fehler
	 */
	public void deleteMovie(long movieId) throws Exception {
		Connection conn = null;
		try { 
			conn = DBConnection.getConnection();
			Movie movie = MovieFactory.findById(movieId);
			if (movie == null) {
				throw new Exception("movie existiert nicht");
			}
			movie.delete();
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
		}
	}

	/**
	 * Ermittelt alle Daten zu einem Movie (d.h. auch Genres und Charaktere) und
	 * trägt diese Daten in einem MovieDTO-Objekt ein.
	 * @param movieId ID des Films der eingelesen wird.
	 * @return MovieDTO-Objekt mit allen Informationen zu dem Film
	 * @throws Exception Z.B. bei Datenbank-Fehlern oder falls der Movie nicht existiert.
	 */
	public MovieDTO getMovie(Long movieId) throws Exception {
		MovieDTO movieDTO = new MovieDTO();
		Movie movie = MovieFactory.findById(movieId);
		Set<String> genres = new HashSet<>(); 
		List<CharacterDTO> characterDTOs = new ArrayList<>();
		GenreManager genreManager = new GenreManager();
		if(movie == null) { return null; }
		genres = genreManager.getGenresByMovie(movieId);
		for (MovieCharacter movieCharacter : MovieFactory.getCharacterByMovieId(movieId)) {
			CharacterDTO characterDTO = new CharacterDTO();
			characterDTO.setAlias(movieCharacter.getAlias());
			characterDTO.setCharacter(movieCharacter.getCharacter());
			characterDTO.setPlayer(PersonFactory.getNameByID(movieCharacter.getPlayerId()));
			movieDTO.addCharacter(characterDTO);
		}
		movieDTO.setGenres(genres);
		movieDTO.setTitle(movie.getTitle());
		movieDTO.setType(movie.getType());
		movieDTO.setYear(movie.getYear());
		return movieDTO;
	}
	
}
