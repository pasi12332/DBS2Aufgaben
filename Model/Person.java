package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Service.DBConnection;

public class Person {
    Long personID;
    String name;

    public void insert() throws SQLException {
        String sql = "INSERT INTO person (name) Values (?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, this.name);

                int affectedRows = pstmt.executeUpdate();

                if(affectedRows == 0) {
                    throw new SQLException("Erstellen von Person fehlgeschlagen, keine Zeilen geändert.");
                }

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.personID = generatedKeys.getLong(1);
                    } else {
                        throw new SQLException("Erstellen von Person fehlgeschlagen, keine ID erhalten.");
                    }
                }
        }
    }
    public void setName(String name) { this.name = name; }

    public Long getPersonID(){ return this.personID; }
}
