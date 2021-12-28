package com.universalcinemas.application.data.language;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.universalcinemas.application.data.film.Film;

@Entity

public class Language {
	private String dub;
	private String subtitles;
	@ManyToMany(mappedBy = "filmLanguages")
    Set<Film> languageFilms;
	@Id
	@GeneratedValue
	private Integer id;
}
 