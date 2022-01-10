package com.universalcinemas.application.data.genre;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universalcinemas.application.data.film.Film;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

}
