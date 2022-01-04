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
	
	public Integer getNum_rows() {
		return num_rows;
	}
	public void setNum_rows(Integer num_rows) {
		this.num_rows = num_rows;
	}
	public Integer getNum_columns() {
		return num_columns;
	}
	public void setNum_columns(Integer num_columns) {
		this.num_columns = num_columns;
	}
	public Business getBusiness() {
		return business;
	}
	public void setBusiness(Business business) {
		this.business = business;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
}
