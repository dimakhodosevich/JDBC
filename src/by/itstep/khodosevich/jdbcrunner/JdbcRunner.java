package by.itstep.khodosevich.jdbcrunner;

import by.itstep.khodosevich.jdbcrunner.util.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class JdbcRunner {

    public static void main(String[] args) throws SQLException {
        String flightId = "2";
//        injection - when someone want to hack your database;
//        String flightId = "2 OR 1=1; DROP TABLE info;";

        List<Long> ticketsByFlightId = getTicketsByFlightId(flightId);
        System.out.println(ticketsByFlightId);
    }

    private static List<Long> getTicketsByFlightId(String flightId) {
        String sql = """
                SELECT id 
                FROM ticket
                WHERE flight_id = %s;
                """.formatted(flightId);
        List<Long> result = new ArrayList<>();
        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement();
        ) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                //null save
                result.add(resultSet.getObject("id", Long.class));
//                result.add(resultSet.getLong("id"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}

