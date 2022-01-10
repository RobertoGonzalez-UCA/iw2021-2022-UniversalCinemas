package com.universalcinemas.application.data.role;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class RoleService extends CrudService<Role,Integer>{

	private RoleRepository roleRepository;

	public Role getDefaultRole() {
		return roleRepository.findByName("ROLE_user").get();
	}
	
	@Autowired
	public RoleService(RoleRepository repository) {
		this.roleRepository = repository;
	}
	
	@Override
	protected JpaRepository<Role, Integer> getRepository() {
		return roleRepository;
	}

	public Collection<Role> findAll() {
		return roleRepository.findAll();
	}
}