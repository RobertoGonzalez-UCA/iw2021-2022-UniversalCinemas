package com.universalcinemas.application.data.city;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universalcinemas.application.data.user.User;

public interface CityRepository extends JpaRepository<City, Integer>{
	
}
