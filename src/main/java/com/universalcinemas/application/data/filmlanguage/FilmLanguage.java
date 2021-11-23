package com.universalcinemas.application.data.filmlanguage;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity

public class FilmLanguage {
	private Integer film_id;
	private Integer language_id;
	@Id
	@GeneratedValue
	private Integer id;
}
