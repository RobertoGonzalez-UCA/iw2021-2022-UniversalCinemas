package com.universalcinemas.application.data.plan;

import org.springframework.data.repository.CrudRepository;

import com.universalcinemas.application.data.film.Film;

public interface PlanRepository extends CrudRepository<Plan, Integer> {

}
