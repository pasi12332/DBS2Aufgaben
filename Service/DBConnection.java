package Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Service-Klasse für Datenbankverbindungen.
 * Verwaltet die Verbindung zur PostgreSQL-Datenbank.
 * Diese Klasse verwendet ein einfaches Singleton-Pattern zum Laden des Drivers.
 */
public class DBConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/db01";
    private static final String USER = "JOLIENESS";
    private static final String PASSWORD = "jonas10";

    static {
        // Load PostgreSQL driver at class initialization time
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found: " + e.getMessage());
        }
    }

    /**
     * Erzeugt und gibt eine neue Verbindung zur PostgreSQL-Datenbank zurück.
     * Jeder Aufruf erzeugt eine neue Verbindung.
     * Der Caller ist verantwortlich, die Verbindung zu schließen.
     * 
     * @return eine neue SQL-Verbindung zur Datenbank
     * @throws SQLException wenn die Verbindung nicht hergestellt werden kann
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}