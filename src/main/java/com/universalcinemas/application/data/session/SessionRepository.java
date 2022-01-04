package com.universalcinemas.application.data.session;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Integer>{
	ArrayList<Session> findByFilm_id(Integer id);
}

