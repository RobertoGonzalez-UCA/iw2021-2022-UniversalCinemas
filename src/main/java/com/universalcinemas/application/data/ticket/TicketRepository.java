package com.universalcinemas.application.data.ticket;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.universalcinemas.application.data.seats.Seats;
import com.universalcinemas.application.data.ticket.Ticket;
import com.universalcinemas.application.data.user.User;

public interface TicketRepository extends CrudRepository<Ticket, Integer>{
	Iterable<Ticket> findBySession_Id(Integer Id);
}
