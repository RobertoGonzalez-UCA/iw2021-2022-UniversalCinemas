package com.universalcinemas.application.data.business;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class BusinessService extends CrudService<Business, Integer>{
	private BusinessRepository businessRepository;
	
	public BusinessService(BusinessRepository businessRepository) {
		this.businessRepository = businessRepository;
	}
	@Override
	protected JpaRepository<Business, Integer> getRepository() {
		return businessRepository;
	}

}
