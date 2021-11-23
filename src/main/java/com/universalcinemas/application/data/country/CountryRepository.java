package com.universalcinemas.application.data.country;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.universalcinemas.application.data.user.User;

public interface CountryRepository extends JpaRepository<User, Integer>{
	
}
