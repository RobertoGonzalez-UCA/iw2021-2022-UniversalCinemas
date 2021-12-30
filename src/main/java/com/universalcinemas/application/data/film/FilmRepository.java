package com.universalcinemas.application.data.film;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.universalcinemas.application.data.film.Film;

public interface FilmRepository extends CrudRepository<Film, Integer>{
	
}  