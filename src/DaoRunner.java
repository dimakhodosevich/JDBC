import by.itstep.khodosevich.jdbcrunner.dao.TicketDao;
import by.itstep.khodosevich.jdbcrunner.entity.Ticket;

import java.math.BigDecimal;
import java.util.Optional;

public class DaoRunner {

    public static void main(String[] args) {
        getAllTickets();


        //findByIdAndUpdate
        //        saveTest();
        //        deleteTest();
    }

    private static void getAllTickets() {
        TicketDao ticketDao = TicketDao.getInstance();
        for (Ticket ticket:
             ticketDao.findALL()) {
            System.out.println(ticket);
            System.out.println();
        }
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

    public static void findByIdAndUpdate() {
        TicketDao ticketDao = TicketDao.getInstance();
        Optional<Ticket> maybeTicket = ticketDao.findById(2L);

        maybeTicket.ifPresent(ticket -> {
            ticket.setCost(BigDecimal.valueOf(188.88));
            ticketDao.update(ticket);
        });

        System.out.println(maybeTicket);
    }
}
