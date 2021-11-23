package com.universalcinemas.application.data.language;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity

public class Language {
	private String dub;
	private String subtitles;
	@Id
	@GeneratedValue
	private Integer id;
}
