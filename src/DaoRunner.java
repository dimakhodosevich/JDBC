import by.itstep.khodosevich.jdbcrunner.dao.TicketDao;
import by.itstep.khodosevich.jdbcrunner.entity.Ticket;

import java.math.BigDecimal;

public class DaoRunner {

    public static void main(String[] args) {
//        saveTest();
        deleteTest();
    }

    public static void deleteTest() {
        TicketDao ticketDao = TicketDao.getInstance();
        Ticket ticket = new Ticket();

        System.out.println(ticketDao.delete(58L));
    }

    public static void saveTest() {
        TicketDao ticketDao = TicketDao.getInstance();
        Ticket ticket = new Ticket();
        ticket.setPassengerNo("12345");
        ticket.setCost(BigDecimal.TEN);
        ticket.setFlightId(7L);
        ticket.setSeatNo("071");
        ticket.setPassengerName("Dima Khodosevich");

        Ticket save = ticketDao.save(ticket);
        System.out.println(save);

    }
}
