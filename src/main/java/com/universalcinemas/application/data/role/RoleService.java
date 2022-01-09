package com.universalcinemas.application.data.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class RoleService extends CrudService<Role,Integer>{

	private RoleRepository repository;

	public Role getDefaultRole() {
		return repository.findByName("ROLE_user").get();
	}
	
	@Autowired
	public RoleService(RoleRepository repository) {
		this.repository = repository;
	}
	
	@Override
	protected JpaRepository<Role, Integer> getRepository() {
		return null;
	}
}