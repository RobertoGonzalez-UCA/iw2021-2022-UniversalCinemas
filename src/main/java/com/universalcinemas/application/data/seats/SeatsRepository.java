package com.universalcinemas.application.data.seats;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.universalcinemas.application.data.seats.SeatsRepository;

public interface SeatsRepository extends JpaRepository<Seats, Integer>{
	
}
