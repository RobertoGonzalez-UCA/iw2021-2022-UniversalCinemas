package com.universalcinemas.application.data.plan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import com.universalcinemas.application.data.film.Film;
import com.universalcinemas.application.data.session.SessionRepository;

@Service
public class PlanService extends CrudService<Plan, Integer>{

	private SessionRepository sessionRepository;

	@Autowired
	public PlanService(SessionRepository sessionRepository) {
		this.sessionRepository = sessionRepository;
	}
	
	@Override
	protected JpaRepository<Plan, Integer> getRepository() {
		// TODO Auto-generated method stub
		return null;
	}

}
