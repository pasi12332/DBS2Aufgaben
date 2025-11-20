package de.hsh.dbs2.imdb.logic;

import java.sql.Connection;
import java.util.List;

import de.hsh.dbs2.imdb.Model.PersonFactory;
import de.hsh.dbs2.imdb.util.DBConnection;

public class PersonManager {
	/**
	 * Liefert eine Liste aller Personen, deren Name den Suchstring enthaelt.
	 * @param name Suchstring
	 * @return Liste mit passenden Personennamen, die in der Datenbank eingetragen sind.
	 * @throws Exception Beschreibt evtl. aufgetretenen Fehler
	 */
	public List<String> getPersonList(String name) throws Exception {
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			return PersonFactory.getPersonListByName(name, conn);
		} catch (Exception e) {
			conn.rollback();
		}
		return null;
				
	}
				
	/**
	 * Liefert die ID einer Person, deren Name genau name ist. Wenn die Person nicht existiert,
	 * wird eine Exception geworfen.
	 * @param name Exakter Name der Person
	 * @return ID der Person
	 * @throws Exception Beschreibt evtl. aufgetretenen Fehler
	 */
	public Long getPerson(String name) throws Exception {
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			return PersonFactory.findByName(name, conn);
		} catch (Exception e) {
			conn.rollback();
		}
		return null;
	}
}
