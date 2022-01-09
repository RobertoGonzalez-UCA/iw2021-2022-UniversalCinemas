package com.universalcinemas.application.data.ticket;

import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<Ticket, Integer>{
	Iterable<Ticket> findBySession_Id(Integer Id);
}
