package de.hsh.dbs2.imdb.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.hsh.dbs2.imdb.persistence.DoesNotExistException;
import de.hsh.dbs2.imdb.util.DBConnection;

public class PersonFactory {

    public static long findByName(String name) throws SQLException, DoesNotExistException {
        String sql = "SELECT personID FROM person WHERE name = ?";
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
    public static List<String> getPersonListByName(String name) throws Exception {
		String sql = "SELECT name FROM person WHERE name ILIKE ?";
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
    
}
