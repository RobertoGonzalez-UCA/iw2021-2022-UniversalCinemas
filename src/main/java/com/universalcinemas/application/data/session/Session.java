package com.universalcinemas.application.data.session;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity

public class Session {
	private Date date_time;
	@Id
	@GeneratedValue
	private Integer id;
}
