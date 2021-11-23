package com.universalcinemas.application.data.country;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity

public class Country {
	private String name;
	@Id
	@GeneratedValue
	private Integer id;
}
