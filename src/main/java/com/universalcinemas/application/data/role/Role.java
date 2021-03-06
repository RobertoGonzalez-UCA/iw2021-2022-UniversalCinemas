package com.universalcinemas.application.data.role;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity

public class Role {
	@NotNull
	private String name;
	@Id
	@GeneratedValue
	private Integer id;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
