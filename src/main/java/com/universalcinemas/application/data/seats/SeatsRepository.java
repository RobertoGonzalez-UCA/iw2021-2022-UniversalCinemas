package com.universalcinemas.application.data.seats;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatsRepository extends JpaRepository<Seats, Integer>{
	Iterable<Seats> findByTicket_Id(Integer Id);
}