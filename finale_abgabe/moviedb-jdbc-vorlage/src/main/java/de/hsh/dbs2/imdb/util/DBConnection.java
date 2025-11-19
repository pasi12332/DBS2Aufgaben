package de.hsh.dbs2.imdb.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
	
	private static Connection conn = null;

	public static Connection getConnection() {
		return conn;
	}

	public static void open() throws SQLException, IOException {
		Properties props = new Properties();
		String currentPath = new java.io.File(".").getCanonicalPath();
		try (FileInputStream fis = new FileInputStream(currentPath+"/moviedb-jdbc-vorlage/src/main/java/de/hsh/dbs2/imdb/util/db.properties")) {
			// Eigenschaften laden
			props.load(fis);

			String uri = props.getProperty("db.uri");
			String username = props.getProperty("db.username");
			String password = props.getProperty("db.password");

			if (conn != null) {
				conn.close();
				conn = null;
			}

			conn = DriverManager.getConnection(uri, username, password);
			conn.setAutoCommit(false);
			System.out.println("Connect durchgefuehrt ....");
		}
	}
}
