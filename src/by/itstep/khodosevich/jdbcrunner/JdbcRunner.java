package by.itstep.khodosevich.jdbcrunner;

import org.postgresql.Driver;

import javax.swing.*;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcRunner {
    public static void main(String[] args) {
        Class<Driver> driverClass = Driver.class;

        String password = "postgres";
        String user = "postgres";
        String url = "jdbc:postgresql://localhost:5433/flight_repository";

        try {
            DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
