package by.itstep.khodosevich.jdbcrunner.dao;

import by.itstep.khodosevich.jdbcrunner.entity.Ticket;
import by.itstep.khodosevich.jdbcrunner.exception.DaoException;
import by.itstep.khodosevich.jdbcrunner.util.ConnectionManager;
import by.itstep.khodosevich.jdbcrunner.util.ConnectionPool;

import java.sql.*;

public class TicketDao {
    private static final TicketDao instance;
    private static String DELETE_SQL = """
            DELETE FROM ticket WHERE id = ?;
            """;

    private static String INSERT_SQL = """
            INSERT INTO ticket (passenger_no, passenger_name, flight_id, seat_no, cost) 
            VALUES (?,?,?,?,?);
            """;

    static {
        instance = new TicketDao();
    }

    private TicketDao() {
    }

    public static synchronized TicketDao getInstance() {
        return instance;
    }

    public boolean delete(Long id) {
        int resultOfRequest;

        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            connection.setAutoCommit(false);
            preparedStatement.setLong(1, id);

            resultOfRequest = preparedStatement.executeUpdate();

            connection.commit();
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }

        return resultOfRequest > 0;
    }

    public Ticket save(Ticket ticket) {


        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);

            preparedStatement.setString(1, ticket.getPassengerNo());
            preparedStatement.setString(2, ticket.getPassengerName());
            preparedStatement.setLong(3, ticket.getFlightId());
            preparedStatement.setString(4, ticket.getSeatNo());
            preparedStatement.setBigDecimal(5, ticket.getCost());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                ticket.setId(generatedKeys.getLong("id"));
            }
            connection.commit();
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }

        return ticket;
    }
}
