package com.universalcinemas.application.data.province;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.universalcinemas.application.data.user.User;

public interface ProvinceRepository extends JpaRepository<Province, Integer>{
	
}
