package com.universalcinemas.application.data.business;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.universalcinemas.application.data.user.User;

public interface BusinessRepository extends JpaRepository<User, Integer>{
	
}
