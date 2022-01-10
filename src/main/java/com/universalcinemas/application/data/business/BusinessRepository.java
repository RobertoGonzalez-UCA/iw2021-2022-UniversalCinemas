package com.universalcinemas.application.data.business;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessRepository extends JpaRepository<Business, Integer>{
	Optional<Business> findById(Integer id);
}
