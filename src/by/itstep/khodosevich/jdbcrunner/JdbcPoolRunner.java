package by.itstep.khodosevich.jdbcrunner;

import by.itstep.khodosevich.jdbcrunner.util.ConnectionManager;
import by.itstep.khodosevich.jdbcrunner.util.ConnectionPool;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcPoolRunner {
    public static void main(String[] args) {
        try{
        checkMetaData();
        } finally {
//            ConnectionPool.closePool();
        }
    }

    public static void checkMetaData() {
        try (Connection connection = ConnectionPool.get()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet catalogs = metaData.getCatalogs();
            while (catalogs.next()) {
                String catalog = catalogs.getString(1);
                ResultSet schemas = metaData.getSchemas();
                while (schemas.next()) {
                    String schem = schemas.getString("TABLE_SCHEM");
                    ResultSet tables = metaData.getTables(catalog, schem,
                            "%", null);
                    if (schem.equals("public")) {
                        while (tables.next()) {

                            if (!tables.getString("TABLE_NAME").equals("aircraft")) {
                                continue;
                            }

                            System.out.println("-------------------");
                            System.out.println(tables.getString("TABLE_NAME"));
                            ResultSet columns =
                                    metaData.getColumns(catalog,
                                            schem,
                                            tables.getString("TABLE_NAME"),
                                            null);


                            while (columns.next()) {

                                System.out.println(columns.getString("COLUMN_NAME"));

                            }
                            System.out.println("-------------------");

                        }
                    }
                }

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Long> getTicketsByFlightId(Long flightId) {
        String sql = """
                SELECT id 
                FROM ticket
                WHERE flight_id = ?;
                """;
        List<Long> result = new ArrayList<>();
        try (Connection connection = ConnectionPool.get();
             PreparedStatement prepareStatement = connection.prepareStatement(sql);
        ) {
            prepareStatement.setLong(1, flightId);

            ResultSet resultSet = prepareStatement.executeQuery();
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

    private static List<Long> getFlightsBetween(LocalDateTime start, LocalDateTime end) {
        String sql = """
                SELECT id 
                FROM flight
                WHERE departure_date BETWEEN ? AND ?;
                """;

        List<Long> result = new ArrayList<>();
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setFetchSize(20); // install size of our date which load from BD
            preparedStatement.setQueryTimeout(10); // install time of our connection
            preparedStatement.setMaxRows(50); // make limit

            System.out.println(preparedStatement);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(start));
            System.out.println(preparedStatement);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(end));
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result.add(resultSet.getLong("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
