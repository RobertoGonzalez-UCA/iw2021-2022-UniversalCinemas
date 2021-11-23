package com.universalcinemas.application.data.ticket;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.universalcinemas.application.data.ticket.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer>{
	
}
