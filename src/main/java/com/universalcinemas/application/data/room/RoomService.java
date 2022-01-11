package com.universalcinemas.application.data.room;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class RoomService extends CrudService<Room, Integer>{
	private RoomRepository roomRepository;
	
	public RoomService(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}
	
	@Override
	protected JpaRepository<Room, Integer> getRepository() {
		return roomRepository;
	}

	public Collection<Room> findAll() {
		return roomRepository.findAll();
	}
}
