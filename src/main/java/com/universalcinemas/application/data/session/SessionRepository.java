package com.universalcinemas.application.data.session;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.universalcinemas.application.data.session.Session;

public interface SessionRepository extends JpaRepository<Session, Integer>{
	
}

