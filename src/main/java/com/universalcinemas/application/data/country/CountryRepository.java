package com.universalcinemas.application.data.country;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Integer>{

	Optional<Country> findByName(String name);
	
}
