package com.universalcinemas.application.data.room;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity

public class Room {
	private Integer num_rows;
	private Integer num_columns;
	@Id
	@GeneratedValue
	private Integer id;
}
