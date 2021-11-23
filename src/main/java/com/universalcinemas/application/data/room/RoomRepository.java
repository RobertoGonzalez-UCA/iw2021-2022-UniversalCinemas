package com.universalcinemas.application.data.room;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.universalcinemas.application.data.room.Room;

public interface RoomRepository extends JpaRepository<Room, Integer>{
	
}
