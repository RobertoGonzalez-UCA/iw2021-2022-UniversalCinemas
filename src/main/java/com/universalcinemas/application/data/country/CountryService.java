package com.universalcinemas.application.data.country;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import com.universalcinemas.application.data.plan.Plan;

@Service
public class CountryService extends CrudService<Country, Integer>{
	
	private CountryRepository countryRepository;
	
	public CountryService(CountryRepository countryRepository) {
		this.countryRepository = countryRepository;
	}
	
	
	public Collection<Country> findAll() {
		return countryRepository.findAll();
	}


	@Override
	protected JpaRepository<Country, Integer> getRepository() {
		return countryRepository;
	}

}
