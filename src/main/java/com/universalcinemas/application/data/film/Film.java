package com.universalcinemas.application.data.film;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.universalcinemas.application.data.city.City;
import com.universalcinemas.application.data.genre.Genre;
import com.universalcinemas.application.data.language.Language;

@Entity

public class Film {
	private String name;
	private String director;
	private String synopsis;
	private String trailerurl;
	private String filmposter;
	private LocalDate releasedate;
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
	public void setName(String name) {this.name = name;}
	public String getSynopsis() {return synopsis;}
	public void setSynopsis(String synopsis) {this.synopsis = synopsis;}
	public String getFilmposter() {return filmposter;}
	public void setFilmposter(String filmposter) {this.filmposter = filmposter;}
	public String getDirector() {return director;}
	public void setDirector(String director) {this.director = director;}
	public LocalDate getReleasedate() {return releasedate;}
	public void setReleasedate(LocalDate releasedate) {this.releasedate = releasedate;}
	public Integer getAgerating() {return agerating;}
	public void setAgerating(Integer agerating) {this.agerating = agerating;}
	public String getTrailerurl() {return trailerurl;}
	public void setTrailerurl(String trailerurl) {this.trailerurl = trailerurl;}
	public Double getRating() {return rating;}
	public void setRating(Double rating) {this.rating = rating;}
	public Genre getGenre() {return genre;}
	public void setGenre(Genre genre) {this.genre = genre;}
}
 