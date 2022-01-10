package com.universalcinemas.application.data.province;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvinceRepository extends JpaRepository<Province, Integer>{

	Collection<Province> findByName(String name);
	
}
