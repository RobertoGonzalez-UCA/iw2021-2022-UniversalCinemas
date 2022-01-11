package com.universalcinemas.application.data.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class SessionService extends CrudService<Session, Integer> {
	
	private SessionRepository sessionRepository;
	
	public SessionService(SessionRepository sessionRepository) {
		this.sessionRepository = sessionRepository;
	}
	
	@Override
	protected JpaRepository<Session, Integer> getRepository() {
		return sessionRepository;
	}
}
