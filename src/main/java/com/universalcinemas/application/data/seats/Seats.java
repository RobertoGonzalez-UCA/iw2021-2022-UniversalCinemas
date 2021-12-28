package com.universalcinemas.application.data.seats;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.universalcinemas.application.data.city.City;
import com.universalcinemas.application.data.ticket.Ticket;

@Entity

public class Seats {
	private Integer row;
	private Integer col;
	@ManyToOne(optional = false)
    private Ticket ticket;
	@Id
	@GeneratedValue
	private Integer id;
}
