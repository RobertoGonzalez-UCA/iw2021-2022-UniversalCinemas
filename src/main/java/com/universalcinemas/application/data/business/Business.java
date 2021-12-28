package com.universalcinemas.application.data.business;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.universalcinemas.application.data.city.City;

@Entity

public class Business {
	private String name;
	private String street;
	@ManyToOne(optional = false)
    private City city;
	@Id
	@GeneratedValue
	private Integer id;
}
