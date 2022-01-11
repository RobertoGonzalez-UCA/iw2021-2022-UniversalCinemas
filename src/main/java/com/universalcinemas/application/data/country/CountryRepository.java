package com.universalcinemas.application.data.country;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universalcinemas.application.data.plan.Plan;
import com.universalcinemas.application.data.user.User;

public interface CountryRepository extends JpaRepository<Country, Integer>{

	Optional<Country> findByName(String name);
	
}
