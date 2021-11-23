package com.universalcinemas.application.data.role;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.universalcinemas.application.data.role.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
	
}
