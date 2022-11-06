package by.itstep.khodosevich.jdbcrunner.exception;

import java.sql.SQLException;

public class DaoException extends RuntimeException {

    public DaoException() {
        super();
    }

    public DaoException(SQLException e) {
        super(e);
    }
}
