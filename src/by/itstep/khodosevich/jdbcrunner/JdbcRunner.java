package by.itstep.khodosevich.jdbcrunner;

import by.itstep.khodosevich.jdbcrunner.util.ConnectionManager;
import org.postgresql.Driver;

import java.sql.SQLException;


public class JdbcRunner {

    public static void main(String[] args) throws SQLException {
        Class<Driver> driverClass = Driver.class;

        try (var connection = ConnectionManager.open();
        ) {
            System.out.println(connection.getTransactionIsolation());
        }
    }
}
