package com.example.midaxus.repositories;

import com.example.midaxus.model.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
/**
 * Repository interface for RoomRepository extends JpaRepository<Room, String>.
 */
public interface RoomRepository extends JpaRepository<Room, String> {
}






















