package com.universalcinemas.application.data.province;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.universalcinemas.application.data.city.City;
import com.universalcinemas.application.data.country.Country;

@Entity

public class Province {
	private String name;
	@ManyToOne(optional = false)
    private Country country;
	@Id
	@GeneratedValue
	private Integer id;
}
