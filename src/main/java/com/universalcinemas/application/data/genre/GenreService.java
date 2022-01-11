package com.universalcinemas.application.data.genre;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class GenreService extends CrudService<Genre, Integer> {
	private GenreRepository genreRepository;
	
	public GenreService(GenreRepository genreRepository) {
		this.genreRepository = genreRepository;
	}
	@Override
	protected JpaRepository<Genre, Integer> getRepository() {
		return genreRepository;
	}
	public Collection<Genre> findAll() {return genreRepository.findAll();}
	
	public String getName(Integer id) {return genreRepository.findById(id).get().getName(); }

}
