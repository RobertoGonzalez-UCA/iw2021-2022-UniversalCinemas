package com.universalcinemas.application.data.seats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class SeatsService extends CrudService<Seats, Integer>{
	private SeatsRepository repository;
	
	@Autowired
	public SeatsService(SeatsRepository repository) {
		this.repository = repository;
	}
	
	@Override
	protected JpaRepository<Seats, Integer> getRepository() {
		return null;
	}
	
	public Iterable<Seats> getAllOccupiedSeatsByTicketId(Integer Id) {
		return repository.findByTicket_Id(Id);
	}
	
	
	public void saveNewOccupiedSeat(Seats seat) {
//		seat.setRow(seat.getRow());
//		seat.setCol(seat.getCol());
//		seat.setTicket(seat.getTicket());
		repository.save(seat);
	}
}