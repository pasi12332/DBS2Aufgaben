package de.hsh.dbs2.imdb.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import de.hsh.dbs2.imdb.persistence.DoesNotExistException;
import de.hsh.dbs2.imdb.util.DBConnection;

public class PersonManager {
	/**
	 * Liefert eine Liste aller Personen, deren Name den Suchstring enthaelt.
	 * @param name Suchstring
	 * @return Liste mit passenden Personennamen, die in der Datenbank eingetragen sind.
	 * @throws Exception Beschreibt evtl. aufgetretenen Fehler
	 */
	public List<String> getPersonList(String name) throws Exception {
		String sql = "SELECT name FROM person WHERE name LIKE ?";
		List<String> resultList = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, "%" + name + "%");
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					String foundName = rs.getString("name");
					resultList.add(foundName);
				}
			}
		}
		return resultList;
	}
				
	/**
	 * Liefert die ID einer Person, deren Name genau name ist. Wenn die Person nicht existiert,
	 * wird eine Exception geworfen.
	 * @param name Exakter Name der Person
	 * @return ID der Person
	 * @throws Exception Beschreibt evtl. aufgetretenen Fehler
	 */
	public int getPerson(String name) throws Exception {
		String sql = "SELECT id FROM person WHERE name = ?";
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, name);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					throw new DoesNotExistException("Person '" + name + "' nicht gefunden");
				}
			}
		}
	}
}
