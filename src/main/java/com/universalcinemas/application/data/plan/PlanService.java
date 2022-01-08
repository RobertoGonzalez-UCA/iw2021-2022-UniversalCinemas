package com.universalcinemas.application.data.plan;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import com.universalcinemas.application.data.film.Film;
import com.universalcinemas.application.data.session.SessionRepository;

@Service
public class PlanService extends CrudService<Plan, Integer>{

	private SessionRepository sessionRepository;
	private PlanRepository planRepository;

	@Autowired
	public PlanService(SessionRepository sessionRepository, PlanRepository planRepository) {
		this.sessionRepository = sessionRepository;
		this.planRepository = planRepository;
	}
	
	@Override
	protected JpaRepository<Plan, Integer> getRepository() {
		// TODO Auto-generated method stub
		return null;
	}

	public Optional<Plan> findById(Integer planId) {
		return planRepository.findById(planId);
	}

	public Iterable<Plan> findAll() {
		return planRepository.findAll();
	}

}
