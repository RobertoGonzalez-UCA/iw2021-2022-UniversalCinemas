package com.universalcinemas.application.data.session;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.universalcinemas.application.data.city.City;
import com.universalcinemas.application.data.film.Film;
import com.universalcinemas.application.data.room.Room;

@Entity

public class Session {
	private Date date_time;
	@ManyToOne(optional = false)
    private Film film;
	@ManyToOne(optional = false)
    private Room room;
	@Id
	@GeneratedValue
	private Integer id;
}
