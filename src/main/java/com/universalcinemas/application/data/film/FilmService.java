package com.universalcinemas.application.data.film;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import com.universalcinemas.application.data.business.Business;
import com.universalcinemas.application.data.session.Session;
import com.universalcinemas.application.data.session.SessionRepository;

@Service
public class FilmService extends CrudService<Film, Integer> {

	private SessionRepository sessionRepository;
	private FilmRepository filmRepository;

	@Autowired
	public FilmService(SessionRepository sessionRepository, FilmRepository filmRepository) {
		this.sessionRepository = sessionRepository;
		this.filmRepository = filmRepository;
	}

	@Override
	protected JpaRepository<Film, Integer> getRepository() {
		return filmRepository;
	}

	public List<Business> obtenerBusinessPelicula(int id) {
		return obtenerBusinessSesiones(sessionRepository.findByFilm_id(id));
	}

	public List<Business> obtenerBusinessSesiones(List<Session> sesiones) {
		return new ArrayList<Business>(sesiones.stream().map(session -> session.getRoom()).collect(Collectors.toSet())
				.stream().map(room -> room.getBusiness()).collect(Collectors.toSet()));
	}

	public List<Session> obtenerSessionsPelicula(int peliculaId) {
		return obtenerSessionsPelicula(peliculaId, null);
	}

	public List<Session> obtenerSessionsPelicula(int peliculaId, LocalDate fechaMin) {
		List<Session> sesiones = sessionRepository.findByFilm_id(peliculaId);

		if (fechaMin != null) {
			LocalDateTime tiempoMin = fechaMin.atStartOfDay();
			sesiones = sesiones.stream().filter(sesion -> sesion.getDate_time().isAfter(tiempoMin))
					.collect(Collectors.toList());
		}

		return sesiones;
	}

	public List<LocalDateTime> fechasSesiones(List<Session> sesiones) {
		return sesiones.stream().map(sesion -> sesion.getDate_time()).collect(Collectors.toList());
	}

	public List<Session> filtrarPorCine(List<Session> sesiones, int idCine) {
		return sesiones.stream().filter(cine -> cine.getRoom().getBusiness().getId() == idCine)
				.collect(Collectors.toList());
	}

	public Optional<Film> findById(Integer filmId) {
		return filmRepository.findById(filmId);
	}

	public Iterable<Film> findAllByOrderByReleasedateDesc() {
		return filmRepository.findAllByOrderByReleasedateDesc();
	}

	public Iterable<Film> findByGenre_Id(int genreId) {
		return filmRepository.findByGenre_Id(genreId);
	}

	public Film loadFilmByName(String name) {
		Optional<Film> film = filmRepository.findByName(name);
		if (film.isPresent()) {
			return film.get();
		} else {
			throw new UsernameNotFoundException(name);
		}
	}

	public List<Film> findAll() {
		return filmRepository.findAll();
	}

	public long countByGenre_Id(Integer id) {
		return filmRepository.countByGenre_Id(id);
	}
}
