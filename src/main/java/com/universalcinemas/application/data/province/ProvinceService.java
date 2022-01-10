package com.universalcinemas.application.data.province;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import com.universalcinemas.application.data.province.Province;
import com.universalcinemas.application.data.province.ProvinceRepository;

@Service
public class ProvinceService extends CrudService<Province, Integer>{
	
	private ProvinceRepository provinceRepository;
	
	public ProvinceService(ProvinceRepository provinceRepository) {this.provinceRepository = provinceRepository;}
	
	
	public Collection<Province> findAll() {return provinceRepository.findAll();}


	@Override
	protected JpaRepository<Province, Integer> getRepository() {return provinceRepository;}


	public Collection<Province> findByName(String name) {return provinceRepository.findByName(name);}

}
