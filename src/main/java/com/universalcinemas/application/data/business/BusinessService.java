package com.universalcinemas.application.data.business;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

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
	public Collection<Business> findAll() {
		return businessRepository.findAll();
	}

	public Business getBusinessById(Integer id) {
		Optional<Business> business = businessRepository.findById(id);
		if (business.isPresent()) {
			return business.get();
		} else {
			throw new NoSuchElementException("No se pudo encontrar ese negocio");
		}
	}
	
}