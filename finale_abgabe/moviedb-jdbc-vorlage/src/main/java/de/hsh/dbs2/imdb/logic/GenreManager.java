package de.hsh.dbs2.imdb.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
    
import de.hsh.dbs2.imdb.Model.Genre;
import de.hsh.dbs2.imdb.Model.GenreFactory;

public class GenreManager {

	/**
	 * Ermittelt eine vollstaendige Liste aller in der Datenbank abgelegten Genres
	 * Die Genres werden alphabetisch sortiert zurueckgeliefert.
	 * @return Alle Genre-Namen als String-Liste
	 * @throws Exception error describing e.g. the database problem
	 */
	public List<String> getGenres() throws Exception {
		List<String> result = new ArrayList<>();
		
		try {
			
			List<Genre> genres = GenreFactory.getAll(); // Alle Genres über die Factory laden
			
			for (Genre g : genres) {  
				result.add(g.getGenre()); // Nur die Namen (Strings) in die Ergebnisliste packen
			}
			return result;
			
		} catch (Exception e) {
			throw e;
		}
		
		
	}
	/**
	 * Ermittelt alle Genres, die einem bestimmten Film zugeordnet sind.
	 * @param movieID Die ID des Films, für den die Genres gesucht werden
	 * @return Ein HashSet mit den Namen der Genres
	 * @throws Exception
	 */
	public HashSet<String> getGenresByMovie(Long movieID) throws Exception {
		HashSet<String> result = new HashSet<>();

    	try {
        
        	List<Genre> genres = GenreFactory.findByMovie(movieID); // Holt die Genre-Objekte passend zur MovieID aus der Factory

        	for (Genre g : genres) {
            result.add(g.getGenre()); // Wandelt die Objekte in Strings um und packt sie ins HashSet
        	}
			return result;

    	} catch (Exception e) {
        	throw e;
    	}

    	
	}
}