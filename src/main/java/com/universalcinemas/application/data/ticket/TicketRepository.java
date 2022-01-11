package com.universalcinemas.application.data.ticket;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
	Iterable<Ticket> findBySession_Id(Integer Id);

	long countBySession(Integer id);
}
