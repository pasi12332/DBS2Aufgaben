import Service.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
//* Temporäre Main Klasse zum Testen der Connection */
public class Main {
    public static void main(String[] args) {
        System.out.println("Testing Database Connection...");
        System.out.println("");
        
        try {
            Connection conn = DBConnection.getConnection();
            System.out.println("✓ Connection successful!");
            System.out.println("  URL: " + conn.getMetaData().getURL());
            System.out.println("  Database: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("  Version: " + conn.getMetaData().getDatabaseProductVersion());
            conn.close();
            System.out.println("✓ Connection closed");
        } catch (SQLException e) {
            System.err.println("✗ Connection failed!");
            System.err.println("");
            System.err.println("Error: " + e.getMessage());
            System.err.println("");
            if (e.getCause() != null) {
                System.err.println("Caused by: " + e.getCause().getMessage());
            }
            e.printStackTrace();
        }
    }
}
