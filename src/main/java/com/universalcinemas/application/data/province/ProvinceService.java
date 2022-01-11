package com.universalcinemas.application.data.province;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class ProvinceService extends CrudService<Province, Integer>{
	
	private ProvinceRepository provinceRepository;
	
	public ProvinceService(ProvinceRepository provinceRepository) {this.provinceRepository = provinceRepository;}
	
	
	public Collection<Province> findAll() {return provinceRepository.findAll();}


	@Override
	protected JpaRepository<Province, Integer> getRepository() {return provinceRepository;}


	public Collection<Province> findByName(String name) {return provinceRepository.findByName(name);}


	public Collection<Province> findByCountry_id(Integer id) {
		return provinceRepository.findByCountry_id(id);
	}

}
