package com.universalcinemas.application.data.business;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity

public class Business {
	private String name;
	private String street;
	private Integer city_id;
	@Id
	@GeneratedValue
	private Integer id;
}
