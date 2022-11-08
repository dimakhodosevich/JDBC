package by.itstep.khodosevich.jdbcrunner.dao;

import by.itstep.khodosevich.jdbcrunner.dto.TicketFilter;
import by.itstep.khodosevich.jdbcrunner.entity.Ticket;
import by.itstep.khodosevich.jdbcrunner.exception.DaoException;
import by.itstep.khodosevich.jdbcrunner.util.ConnectionManager;
import by.itstep.khodosevich.jdbcrunner.util.ConnectionPool;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static by.itstep.khodosevich.jdbcrunner.entity.Ticket.buildTicket;

public class TicketDao {
    private static final TicketDao instance;
    private static String DELETE_SQL = """
            DELETE FROM ticket WHERE id = ?;
            """;

    private static String INSERT_SQL = """
            INSERT INTO ticket (passenger_no, passenger_name, flight_id, seat_no, cost) 
            VALUES (?,?,?,?,?);
            """;

    private static String UPDATE_SQL = """
            UPDATE ticket
            SET passenger_no = ?, 
            passenger_name = ?, 
            flight_id = ?, 
            seat_no = ?, 
            cost = ?
            WHERE id = ?""";

    private static String FIND_ALL_SQL = """
                    SELECT id, 
                    passenger_no, 
                    passenger_name, 
                    flight_id, 
                    seat_no, 
                    cost
                    FROM ticket
            """;

    private static String FIND_BY_ID = FIND_ALL_SQL + """
            WHERE id = ?""";

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
            if (generatedKeys.next()) {
                ticket.setId(generatedKeys.getLong("id"));
            }
            connection.commit();
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }

        return ticket;
    }

    public void update(Ticket ticket) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            connection.setAutoCommit(false);

            preparedStatement.setString(1, ticket.getPassengerNo());
            preparedStatement.setString(2, ticket.getPassengerName());
            preparedStatement.setLong(3, ticket.getFlightId());
            preparedStatement.setString(4, ticket.getSeatNo());
            preparedStatement.setBigDecimal(5, ticket.getCost());
            preparedStatement.setLong(6, ticket.getId());
            preparedStatement.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<Ticket> findById(Long id) {
        Ticket ticket = null;
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            connection.setAutoCommit(false);

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                ticket = new Ticket(resultSet.getLong("id"),
                        resultSet.getString("passenger_no"),
                        resultSet.getString("passenger_name"),
                        resultSet.getLong("flight_id"),
                        resultSet.getString("seat_no"),
                        resultSet.getBigDecimal("cost"));
            }

            connection.commit();
        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return Optional.ofNullable(ticket);
    }

    public ArrayList<Ticket> findALL() {
        ArrayList<Ticket> tickets = new ArrayList<>();

        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            connection.setAutoCommit(false);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Ticket newTicket = new Ticket(resultSet.getLong("id"),
                        resultSet.getString("passenger_no"),
                        resultSet.getString("passenger_name"),
                        resultSet.getLong("flight_id"),
                        resultSet.getString("seat_no"),
                        resultSet.getBigDecimal("cost"));

                tickets.add(newTicket);
            }

            connection.commit();
        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return tickets;
    }

    public List<Ticket> findAll(TicketFilter filter) {
        List<Object> parameters = new ArrayList<>();
        parameters.add(filter.limit());
        parameters.add(filter.offset());

        String sql = FIND_ALL_SQL + """
                LIMIT ? 
                OFFSET ?""";
        ArrayList<Ticket> tickets = new ArrayList<>();

        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            connection.setAutoCommit(false);

            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i+1, parameters.get(i));
            }
//            This is our prepare statement:
//            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()){
                tickets.add(buildTicket(resultSet));

            }
            connection.commit();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return tickets;
    }

}
