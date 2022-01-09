package com.universalcinemas.application.data.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;


@Service
public class TicketService extends CrudService<Ticket, Integer>{
	private TicketRepository repository;

	@Autowired
	public TicketService(TicketRepository repository) {
		this.repository = repository;
	}
	
	@Override
	protected JpaRepository<Ticket, Integer> getRepository() {
		return null;
	}
	
	public void saveNewTicket(Ticket ticket) {
		ticket.setPrice(ticket.getPrice());
		ticket.setDiscount(ticket.getDiscount());
		ticket.setSession(ticket.getSession());
		repository.save(ticket);
	}
	
	public Iterable<Ticket> findBySessionId(Integer id) {
		return repository.findBySession_Id(id);
	}
}
