package com.universalcinemas.application.data.film;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.universalcinemas.application.data.genre.Genre;
import com.universalcinemas.application.data.language.Language;
import com.universalcinemas.application.data.plan.Plan;

@Entity

public class Film {
	private String name;
	private String director;
	private String synopsis;
	private String trailerurl;
	private String filmposter;
	private Date releasedate;
	private Integer agerating;
	private Double rating;
	@ManyToOne
	private Genre genre;
	@ManyToMany
    Set<Language> filmLanguages;
	@Id
	@GeneratedValue
	private Integer id;
	public Integer getId() {return id;}
	public String getName() {return name;}
	public String getFilmPoster() {return filmposter;}
	public String getSynopsis() {return synopsis;}
}
 