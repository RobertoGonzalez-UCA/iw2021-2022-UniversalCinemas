package com.universalcinemas.application.data.business;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.universalcinemas.application.data.city.City;

@Entity

public class Business {
	@NotNull
	private String name;
	@NotNull
	private String street;

	@NotNull
	@ManyToOne(optional = false)
	private City city;

	@Id
	@GeneratedValue
	private Integer id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
		
	public String getCityName() {
		return city.getName();
	}
	
	public String getProvinceName() {
		return city.getProvince().getName();
	}
	
	public String getCountryName() {
		return city.getProvince().getCountry().getName();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
