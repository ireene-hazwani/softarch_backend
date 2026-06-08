package db;

import java.sql.*;

public class DBConnection {

    // 1. Dynamically identify the database location host address
    // If DB_HOST environment variable exists (set by docker-compose), use it ("db").
    // Otherwise, fall back to "localhost" for local NetBeans server deployments.
    private static final String HOST = System.getenv("DB_HOST") != null 
            ? System.getenv("DB_HOST") 
            : "localhost";

    private static final String URL = "jdbc:mysql://" + HOST + ":3306/softwarearchitecture";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Matches Docker Compose MYSQL_ALLOW_EMPTY_PASSWORD

    public static Connection getConnection() {
        try {
            // Load the modern MySQL JDBC Driver class
            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (Exception e) {
            System.err.println("Database Connection Failed! Target URL: " + URL);
            e.printStackTrace();
            return null;
        }
    }
}