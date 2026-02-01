package dao;

// Utility class for managing database connections

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/dispatch_system";
    private static final String USER = "root";
    private static final String PASSWORD = "1$isBest";

}
