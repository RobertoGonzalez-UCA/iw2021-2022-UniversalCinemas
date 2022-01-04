package com.universalcinemas.application.data.business;

import java.util.ArrayList;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universalcinemas.application.data.user.User;

public interface BusinessRepository extends JpaRepository<User, Integer>{
	
}
