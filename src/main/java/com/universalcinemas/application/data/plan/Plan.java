package com.universalcinemas.application.data.plan;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.universalcinemas.application.data.genre.Genre;

@Entity

public class Plan {
	private String name;
	private String description;
	private Integer percent;
	private Integer price;
	@ManyToOne
	private Genre genre;
	@Id
	@GeneratedValue
	private Integer id;
	public String getDescription() {return description;}
	public String getName() {return name;}
	public Integer getId() {return id;}
	public Integer getPrice() {return price;}
	public Integer getPercent() {return percent;}
}
 