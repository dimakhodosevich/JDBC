package by.itstep.khodosevich.jdbcrunner;

import by.itstep.khodosevich.jdbcrunner.util.ConnectionManager;
import org.postgresql.Driver;

import java.sql.SQLException;
import java.sql.Statement;


public class JdbcRunner {

    public static void main(String[] args) throws SQLException {
        Class<Driver> driverClass = Driver.class;
        String sql = """
                    UPDATE info 
                    SET data = 'TestTest'
                    WHERE id = 5
                    RETURNING *
                    ;
                """;

        try (var connection = ConnectionManager.open();
             var statement = connection.createStatement();
        ) {
            var executeResult = statement.execute(sql);
            System.out.println(connection.getSchema());
            System.out.println(connection.getTransactionIsolation());
            System.out.println(executeResult);
            System.out.println(statement.getUpdateCount());

        }
    }
}
