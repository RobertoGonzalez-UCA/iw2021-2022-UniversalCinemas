package com.universalcinemas.application.data.room;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.universalcinemas.application.data.business.Business;
import com.universalcinemas.application.data.city.City;

@Entity

public class Room {
	private Integer num_rows;
	private Integer num_columns;
	@ManyToOne(optional = false)
    private Business business;
	@Id
	@GeneratedValue
	private Integer id;
}
