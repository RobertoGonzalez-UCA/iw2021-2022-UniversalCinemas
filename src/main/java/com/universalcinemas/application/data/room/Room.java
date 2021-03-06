package com.universalcinemas.application.data.room;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.universalcinemas.application.data.business.Business;

@Table(
	    uniqueConstraints=
	        @UniqueConstraint(columnNames={"business_id", "num_room"})
	)

@Entity
public class Room {
	
	@NotNull
	private Integer num_rows;
	@NotNull
	private Integer num_columns;
	@NotNull
	private Integer num_room;
	@NotNull
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
	public Integer getNum_room() {
		return num_room;
	}
	public void setNum_room(Integer num_room) {
		this.num_room = num_room;
	}
}
