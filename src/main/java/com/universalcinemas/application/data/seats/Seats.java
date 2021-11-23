package com.universalcinemas.application.data.seats;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity

public class Seats {
	private Integer row;
	private Integer column;
	@Id
	@GeneratedValue
	private Integer id;
}
