package com.universalcinemas.application.data.ticket;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity

public class Ticket {
	private Double price;
	private Integer discount;
	@Id
	@GeneratedValue
	private Integer id;
}
