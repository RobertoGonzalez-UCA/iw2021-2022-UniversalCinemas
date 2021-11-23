package com.universalcinemas.application.data.session;

import java.sql.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Session {
	private Date date_time;
	@Id
	@GeneratedValue
	private Integer id;
}
