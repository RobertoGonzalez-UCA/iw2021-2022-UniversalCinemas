package com.universalcinemas.application.data.film;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import com.universalcinemas.application.data.language.Language;

@Entity

public class Film {
	private String name;
	private String director;
	private String synopsis;
	private String trailerurl;
	private String filmposter;
	private String gender;
	private Date releasedate;
	private Integer agerating;
	private Double rating;
	@ManyToMany
    Set<Language> filmLanguages;
	@Id
	@GeneratedValue
	private Integer id;
	public String getName() {return name;}
	public String getFilmPoster() {return filmposter;}
}
 