package com.universalcinemas.application.data.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.artur.helpers.CrudService;

public class SessionService extends CrudService<Session, Integer> {
	
	@Override
	protected JpaRepository<Session, Integer> getRepository() {
		return null;
	}
}
