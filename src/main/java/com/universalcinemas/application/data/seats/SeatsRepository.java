package com.universalcinemas.application.data.seats;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.universalcinemas.application.data.seats.SeatsRepository;
import com.universalcinemas.application.data.ticket.Ticket;
import com.universalcinemas.application.data.user.User;

public interface SeatsRepository extends JpaRepository<Seats, Integer>{
	Iterable<Seats> findByTicket_Id(Integer Id);
}