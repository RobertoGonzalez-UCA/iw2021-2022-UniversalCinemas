package com.universalcinemas.application.data.seats;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.universalcinemas.application.data.ticket.Ticket;

@Entity

public class Seats {
	@NotNull
	private Integer roww;
	@NotNull
	private Integer col;
	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(referencedColumnName = "id")
    private Ticket ticket;
	@Id
	@GeneratedValue
	private Integer id;
	
	public Seats() {}
	
	public Seats(Integer id) {
		this.id = id;
	}
	
	public Seats(Integer row, Integer col, Ticket ticket) {
		this.roww = row;
		this.col = col;
		this.ticket = ticket;
	}
	
	public Integer getId() {return id;}
	public Integer getCol() {return col;}
	public Integer getRow() {return roww;}
	public Ticket getTicket() {return ticket;}
	
	public void setCol(Integer col) {
		this.col = col;
	}
	public void setRow(Integer row) {
		this.roww = row;
	}
	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}
}
