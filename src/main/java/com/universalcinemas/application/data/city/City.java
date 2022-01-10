package com.universalcinemas.application.data.city;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.universalcinemas.application.data.province.Province;
import com.vaadin.flow.component.Component;

@Entity

public class City {
	private String name;
	@ManyToOne(optional = false)
    private Province province;
	@Id
	@GeneratedValue
	private Integer id;
	public Integer getId() {return id;}
	public String getName() {return name;}
}