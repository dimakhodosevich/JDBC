package by.itstep.khodosevich.jdbcrunner.dto;

public record TicketFilter(int limit,
                           int offset,
                           String passengerName,
                           String seatNo) {

}
