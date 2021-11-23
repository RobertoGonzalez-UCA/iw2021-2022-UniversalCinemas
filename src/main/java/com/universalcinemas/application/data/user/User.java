package com.universalcinemas.application.data.user;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity

public class User {
	private String name;
	private String surname;
	private String email;
	private String password;
	private String urlprofileimage;
	private Date dateofbirth;
	private String phonenumber;
	@Id
	@GeneratedValue
	private Integer id;
}
