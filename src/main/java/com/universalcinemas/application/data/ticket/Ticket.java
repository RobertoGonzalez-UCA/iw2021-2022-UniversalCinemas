package com.universalcinemas.application.data.ticket;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.universalcinemas.application.data.city.City;
import com.universalcinemas.application.data.session.Session;

@Entity

public class Ticket {
	private Double price;
	private Integer discount;
	@ManyToOne(optional = false)
    private Session session;
	@Id
	@GeneratedValue
	private Integer id;
}
