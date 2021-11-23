package com.universalcinemas.application.data.film;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.universalcinemas.application.data.film.Film;

public interface FilmRepository extends JpaRepository<Film, Integer>{
	
}