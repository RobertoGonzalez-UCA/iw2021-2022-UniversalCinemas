package com.universalcinemas.application.data.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity

public class User {
	private String name;
	private String surname;
	@Id
	@GeneratedValue
	private Integer id;
}
