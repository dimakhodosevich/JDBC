package by.itstep.khodosevich.jdbcrunner.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String PASSWORD = "postgres";
    private static final String USER = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5433/flight_repository";

    static {
        loadDriver();
    }

    private static void loadDriver() {
        //            before java 1.8 you have to manually load class
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private ConnectionManager() {

    }

    public static Connection open() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
