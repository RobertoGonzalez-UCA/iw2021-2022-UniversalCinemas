package com.universalcinemas.application.data.city;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class CityService extends CrudService<City, Integer> {
	private CityRepository cityRepository;
	
	public CityService(CityRepository cityRepository) {
		this.cityRepository = cityRepository;
	}
	
	@Override
	protected JpaRepository<City, Integer> getRepository() {
		return cityRepository;
	}

	public Collection<City> findAll() {
		return cityRepository.findAll();
	}

	public Collection<City> findByName(String name) {return cityRepository.findByName(name);}

}
