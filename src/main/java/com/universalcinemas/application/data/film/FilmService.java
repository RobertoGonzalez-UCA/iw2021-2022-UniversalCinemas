package com.universalcinemas.application.data.film;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import com.universalcinemas.application.data.business.Business;
import com.universalcinemas.application.data.session.Session;
import com.universalcinemas.application.data.session.SessionRepository;

@Service
public class FilmService extends CrudService<Film, Integer> {

	private SessionRepository sessionRepository;
	
	@Autowired
	public FilmService(SessionRepository sessionRepository) {
		this.sessionRepository = sessionRepository;
	}
	
	@Override
	protected JpaRepository<Film, Integer> getRepository() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Business> obtenerBusinessPelicula(int id) {
		return obtenerBusinessSesiones(sessionRepository.findByFilm_id(id));
	}
	
	public List<Business> obtenerBusinessSesiones(List<Session> sesiones) {
		return new ArrayList<Business>(sesiones.stream()
				.map(session -> session.getRoom())
				.collect(Collectors.toSet())
				.stream()
				.map(room -> room.getBusiness())
				.collect(Collectors.toSet()));
	}
	
	public List<Session> obtenerSessionsPelicula(int peliculaId) {
		return obtenerSessionsPelicula(peliculaId, null);
	}
	
	public List<Session> obtenerSessionsPelicula(int peliculaId, LocalDate fechaMin) {
		List<Session> sesiones = sessionRepository.findByFilm_id(peliculaId);
		
		if(fechaMin != null) {
			LocalDateTime tiempoMin = fechaMin.atStartOfDay();
			sesiones = sesiones.stream()
					.filter(sesion -> sesion.getDate_time().isAfter(tiempoMin))
					.collect(Collectors.toList());
		}
		
		return sesiones;
	}
}
