package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    public Connection getConn() {
        String url = "jdbc:mysql://localhost:3306/course_registering_app";
        String username = "admin";
        String password = "@Tungta2001";
        System.out.println("Connecting database...");
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

}
