package com.universalcinemas.application.data.film;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.universalcinemas.application.data.genre.Genre;
import com.universalcinemas.application.data.role.Role;

public interface FilmRepository extends CrudRepository<Film, Integer>{
	Optional<Film> findById(Integer id);
	Iterable<Film> findAllByOrderByRating();
	Iterable<Film> findAllByOrderByReleasedateDesc();
	Iterable<Film> findByGenre_Id(Integer Id);
}  