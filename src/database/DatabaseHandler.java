package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/test";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            establishConnection();
        }

        return connection;
    }

    private static void establishConnection() {
        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            System.out.println("connected to the database.");
        } catch (SQLException e) {

            System.out.println("failed to connect to the database.");
            e.printStackTrace();
        }
    }

    private static DatabaseHandler instance;

    public static DatabaseHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseHandler();
        }
        return instance;
    }

    private DatabaseHandler() {
    }
}
