package com.universalcinemas.application.data.session;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.universalcinemas.application.data.film.Film;
import com.universalcinemas.application.data.room.Room;

@Entity

public class Session {
	@NotNull
	private LocalDateTime date_time;
	@NotNull
	@ManyToOne(optional = false)
    private Film film;
	@NotNull
	@ManyToOne(optional = false)
    private Room room;
	@Id
	@GeneratedValue
	private Integer id;
	
	public Session() {}
	
	public Session(LocalDateTime date_time, Film film, Room room) {
		this.date_time = date_time;
		this.film = film;
		this.room = room;
	}
	
	public LocalDateTime getDate_time() {
		return date_time;
	}
	public void setDate_time(LocalDateTime date_time) {
		this.date_time = date_time;
	}
	public Film getFilm() {
		return film;
	}
	public void setFilm(Film film) {
		this.film = film;
	}
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
}
