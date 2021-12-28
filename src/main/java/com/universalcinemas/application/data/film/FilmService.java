package com.universalcinemas.application.data.film;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import com.universalcinemas.application.data.user.User;

@Service
public class FilmService extends CrudService<Film, Integer> {

	@Override
	protected JpaRepository<Film, Integer> getRepository() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Set<Film> getFilms(){
		
		return null;
	}

}
