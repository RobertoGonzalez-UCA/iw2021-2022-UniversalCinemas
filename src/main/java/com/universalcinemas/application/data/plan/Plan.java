package com.universalcinemas.application.data.plan;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.universalcinemas.application.data.genre.Genre;
import com.universalcinemas.application.data.language.Language;

@Entity

public class Plan {
	private String name;
	private String description;
	private Integer percent;
	@ManyToOne
	private Genre genre;
	@Id
	@GeneratedValue
	private Integer id;
	public String getDescription() {return description;}
	public String getName() {return name;}
}
 