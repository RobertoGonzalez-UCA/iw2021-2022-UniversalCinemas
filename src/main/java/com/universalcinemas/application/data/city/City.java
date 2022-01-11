package com.universalcinemas.application.data.city;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.universalcinemas.application.data.province.Province;

@Entity

public class City {
	@NotNull
	private String name;

	@NotNull
	@ManyToOne(optional = false)
	private Province province;

	@Id
	@GeneratedValue
	private Integer id;

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}
}