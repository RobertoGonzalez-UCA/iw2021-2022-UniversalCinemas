package com.universalcinemas.application.data.language;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Language {
	private String dub;
	private String subtitles;
	@Id
	@GeneratedValue
	private Integer id;
}
