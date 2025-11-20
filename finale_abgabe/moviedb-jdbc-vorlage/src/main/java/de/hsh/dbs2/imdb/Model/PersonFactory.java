package de.hsh.dbs2.imdb.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.hsh.dbs2.imdb.persistence.DoesNotExistException;

/**
 * Factory class for person-related database operations.
 */
public class PersonFactory {

	/**
	 * Finds a person by exact name and returns their personID.
	 *
	 * @param name the exact name of the person to search for
	 * @return the personID of the found person
	 * @throws SQLException if a database access error occurs
	 * @throws DoesNotExistException if no person with the given name exists
	 */
	public static long findByName(String name, Connection conn) throws SQLException, DoesNotExistException {
		String sql = "SELECT personID FROM person WHERE name = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, name);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getLong(1);
				} else {
					throw new DoesNotExistException("Person '" + name + "' nicht gefunden");
				}
			}
		}
	}


	/**
	 * Gibt den Namen einer Person anhand ihrer ID zurück.
	 *
	 * Diese Methode führt eine SQL-Abfrage auf der Tabelle {@code person} aus und sucht nach einem Eintrag
	 * mit der angegebenen {@code playerID}. Wird ein entsprechender Datensatz gefunden, wird der Wert aus
	 * der Spalte {@code name} zurückgegeben. Existiert kein Eintrag mit dieser ID, wird eine
	 * {@link DoesNotExistException} ausgelöst.
	 *
	 * @param playerID Die ID der gesuchten Person.
	 * @param conn     Eine gültige Datenbankverbindung, über die die Abfrage ausgeführt wird.
	 * @return Der Name der Person mit der angegebenen ID.
	 * @throws DoesNotExistException Wenn keine Person mit der übergebenen ID existiert.
	 * @throws Exception Wenn ein Fehler beim Zugriff auf die Datenbank auftritt.
	 */
	public static String getNameByID(Long playerID, Connection conn) throws Exception {
		String sql = "SELECT * FROM person WHERE personID = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, playerID);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getString("name");
				} else {
					throw new DoesNotExistException("Person '" + playerID + "' nicht gefunden");
				}
			}
		}
	}

	/**
	 * Returns a list of all person names containing the given search string (case-insensitive).
	 *
	 * @param name the search string to look for in person names
	 * @return a list of matching person names
	 * @throws Exception if a database access error occurs
	 */
	public static List<String> getPersonListByName(String name, Connection conn) throws Exception {
		String sql = "SELECT name FROM person WHERE name ILIKE ?";
		List<String> resultList = new ArrayList<>();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
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

}
