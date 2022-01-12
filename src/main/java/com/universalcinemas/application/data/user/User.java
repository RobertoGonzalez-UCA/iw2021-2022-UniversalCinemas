package com.universalcinemas.application.data.user;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.universalcinemas.application.data.plan.Plan;
import com.universalcinemas.application.data.role.Role;

@Entity
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;
	@NotNull
	private String name;
	@NotNull
	private String surname;
	@NotNull
	private String email;
	@NotNull
	private String password;
	private String urlprofileimage;
	@NotNull
	private LocalDate dateofbirth;
	@NotNull
	private String phonenumber;

	@NotNull
	@ManyToOne
	private Role role;

	@ManyToOne
	private Plan plan;

	@Id
	@GeneratedValue
	private Integer id;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		roles.add(new SimpleGrantedAuthority(this.getRole().getName()));

		return roles;
	}

	public User() {
	}

	public User(String name, String surname, String email, String dateofBirth, String phonenumber, String password) {

		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phonenumber = phonenumber;

		// Convert from String to LocalDate
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		formatter = formatter.withLocale(new Locale("es", "ES")); // Locale specifies human language for translating,
																	// and cultural norms for lowercase/uppercase and
																	// abbreviations and such. Example: Locale.US or
																	// Locale.CANADA_FRENCH
		LocalDate date = LocalDate.parse(dateofBirth, formatter);

		this.dateofbirth = date;
		this.password = password;

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

	// REVISAR
	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	// -------

	public LocalDate getDateOfBirth() {
		return dateofbirth;
	}

	public void setDateOfBirth(LocalDate dateofbirth) {
		this.dateofbirth = dateofbirth;
	}

	// REVISAR
	public LocalDate getDateofbirth() {
		return dateofbirth;
	}

	public void setDateofbirth(LocalDate dateofbirth) {
		this.dateofbirth = dateofbirth;
	}
	// -------

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
		return name;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}
}
