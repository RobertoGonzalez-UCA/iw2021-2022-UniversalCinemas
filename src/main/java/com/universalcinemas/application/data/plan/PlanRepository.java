package com.universalcinemas.application.data.plan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface PlanRepository extends JpaRepository<Plan, Integer> {

}
