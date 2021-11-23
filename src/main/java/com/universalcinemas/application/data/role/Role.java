package com.universalcinemas.application.data.role;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Role {
	private String name;
	@Id
	@GeneratedValue
	private Integer id;
}
