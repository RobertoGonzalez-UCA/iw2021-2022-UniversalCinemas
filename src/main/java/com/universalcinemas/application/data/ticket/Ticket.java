package com.universalcinemas.application.data.ticket;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.universalcinemas.application.data.session.Session;

@Entity

public class Ticket {
	private Double price;
	private Integer discount;
	@ManyToOne(optional = false)
    private Session session;
	@Id
	@GeneratedValue
	private Integer id;
	
	public Ticket() {}	
	
	public Ticket(Double price, Integer discount, Session session) {
		this.price = price;
		this.discount = discount;
		this.session = session;
	}
	
	public Integer getId() {return id;}
	public Double getPrice() {return price;}
	public Integer getDiscount() {return discount;}
	public Session getSession() {return session;}
	
	public void setPrice(Double price) {
		this.price = price;
	}
	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	
//	public Ticket(Double name, String surname, String email, String dateofBirth, String phonenumber, String password){
//			
//		this.name = name;
//		this.surname = surname;
//		this.email = email;
//		this.phonenumber = phonenumber;
//		
//		// Convert from String to LocalDate
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//		formatter = formatter.withLocale( new Locale ( "es" , "ES" ));  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
//		LocalDate date = LocalDate.parse(dateofBirth, formatter);
//    
//		this.dateofbirth = date;
//		this.password = password;
//	
//	}
}
