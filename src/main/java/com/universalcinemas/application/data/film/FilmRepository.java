package com.universalcinemas.application.data.film;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface FilmRepository extends CrudRepository<Film, Integer>{
	Optional<Film> findById(Integer id);
	Iterable<Film> findAllByOrderByRating();
	Iterable<Film> findAllByOrderByReleasedateDesc();
	Iterable<Film> findByGenre_Id(Integer Id);
}  