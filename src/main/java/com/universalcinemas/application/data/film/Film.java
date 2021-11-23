package com.universalcinemas.application.data.film;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity

public class Film {
	private String name;
	private String director;
	private String synopsis;
	private String trailerurl;
	private String gender;
	private Date releasedate;
	private Integer agerating;
	private Double rating;
	@Id
	@GeneratedValue
	private Integer id;
}
