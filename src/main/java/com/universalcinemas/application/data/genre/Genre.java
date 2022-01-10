package com.universalcinemas.application.data.genre;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;



@Entity

public class Genre {
	private String name;
	@Id
	@GeneratedValue
	private Integer id;
	public String getName() {return name;}
	public Integer getId() {return id;}
}
