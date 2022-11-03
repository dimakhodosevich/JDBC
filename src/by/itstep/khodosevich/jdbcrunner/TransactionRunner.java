package by.itstep.khodosevich.jdbcrunner;

import by.itstep.khodosevich.jdbcrunner.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class TransactionRunner {
    public static void main(String[] args) {

        String deleteTicketSql = """
                DELETE FROM ticket WHERE flight_id = ?""";

        Long flightId = 9L;
        String deleteFlightSql = """
                DELETE FROM flight WHERE id = ?""";

        Connection connection = null;
        PreparedStatement deleteFlightStatement = null;
        PreparedStatement deleteTicketStatement = null;

        try {
            connection = ConnectionManager.open();
            deleteFlightStatement = connection.prepareStatement(deleteFlightSql);
            deleteTicketStatement = connection.prepareStatement(deleteTicketSql);
            connection.setAutoCommit(false); // on default = true; do it in all operations;

            deleteFlightStatement.setLong(1, flightId);
            deleteTicketStatement.setLong(1, flightId);

            deleteTicketStatement.executeUpdate();

            if (true) {
                throw new RuntimeException("Oops");
            }

            deleteFlightStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if (deleteFlightSql != null) {
                try {
                    deleteFlightStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if (deleteTicketSql != null) {
                try {
                    deleteTicketStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
