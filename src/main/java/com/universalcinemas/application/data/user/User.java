package com.universalcinemas.application.data.user;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.universalcinemas.application.data.role.Role;

@Entity
public class User implements UserDetails {
	private String name;
	private String surname;
	private String email;
	private String password;
	private String urlprofileimage;
	private LocalDate dateofbirth;
	private String phonenumber;
	
	@ManyToOne
	private Role role;
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		roles.add(new SimpleGrantedAuthority(this.getRole().getName()));

		return roles;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phonenumber;
	}

	public void setPhone(String phone) {
		this.phonenumber = phone;
	}

	public LocalDate getDateOfBirth() {
		return dateofbirth;
	}

	public void setDateOfBirth(LocalDate dateofbirth) {
		this.dateofbirth = dateofbirth;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		
		return this.password;
	}

	public String getUrlprofileimage() {
		return urlprofileimage;
	}

	public void setUrlprofileimage(String urlprofileimage) {
		this.urlprofileimage = urlprofileimage;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getUsername() {
		return null;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
