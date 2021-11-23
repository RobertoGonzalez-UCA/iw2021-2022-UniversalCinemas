package com.universalcinemas.application.data.ticket;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Ticket {
	private Double price;
	private Integer discount;
	@Id
	@GeneratedValue
	private Integer id;
}
