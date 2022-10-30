package by.itstep.khodosevich.jdbcrunner;

import by.itstep.khodosevich.jdbcrunner.util.ConnectionManager;
import org.postgresql.Driver;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class JdbcRunner {

    public static void main(String[] args) throws SQLException {
        Class<Driver> driverClass = Driver.class;
        String sql = """
                    SELECT * 
                    FROM ticket;
                """;

        try (var connection = ConnectionManager.open();
//             using for manipulate our results(change it)
//             var statement =
//                     connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

             var statement = connection.createStatement();
        ) {
            var executeResult = statement.executeQuery(sql);
            System.out.println(connection.getSchema());
            System.out.println(connection.getTransactionIsolation());
            System.out.println(executeResult);
            while (executeResult.next()) {
                System.out.println(executeResult.getLong("id"));
                System.out.println(executeResult.getString("passenger_no"));
                System.out.println(executeResult.getBigDecimal("cost"));
//                when add arguments to our connection statement below
//                executeResult.updateBigDecimal("cost", BigDecimal.valueOf(1.22222));

                System.out.println("------------");
            }
        }
    }
}
