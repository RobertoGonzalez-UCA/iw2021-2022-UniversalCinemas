package com.universalcinemas.application.data.city;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.universalcinemas.application.data.province.Province;

@Entity

public class City {
	private String name;
	@ManyToOne(optional = false)
    private Province province;
	@Id
	@GeneratedValue
	private Integer id;
}