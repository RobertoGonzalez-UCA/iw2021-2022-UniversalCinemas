package com.universalcinemas.application.data.province;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity

public class Province {
	private String name;
	private Integer country_id;
	@Id
	@GeneratedValue
	private Integer id;
}
