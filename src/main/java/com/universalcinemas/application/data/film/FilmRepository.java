package com.universalcinemas.application.data.film;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmRepository extends JpaRepository<Film, Integer>{
	Optional<Film> findById(Integer id);
	Iterable<Film> findAllByOrderByRating();
	Iterable<Film> findAllByOrderByReleasedateDesc();
	Iterable<Film> findByGenre_Id(Integer Id);
	Optional<Film> findByName(String name);
	List<Film> findAll();
}  