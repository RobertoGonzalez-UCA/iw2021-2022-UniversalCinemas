package com.universalcinemas.application.data.city;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity

public class City {
	private String name;
	private Integer province_id;
	@Id
	@GeneratedValue
	private Integer id;
}