package dao;

// Utility class for managing database connections

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/dispatch_system";
    private static final String USER = "root";
    private static final String PASSWORD = "1$isBest";

    private static Connection connection = null;

    // Private constructor to prevent instantiation
    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load driver class (optional in newer JDBC versions but good for compatibility)
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                System.err.println("MySQL JDBC Driver not found!");
                e.printStackTrace();
            }
        }
        return connection;
    }
}

}
