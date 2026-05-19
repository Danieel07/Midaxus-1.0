package com.example.midaxus.repositories;

import com.example.midaxus.model.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
/**
 * Repository interface for ScheduleRepository extends JpaRepository<Schedule, String>.
 */
public interface ScheduleRepository extends JpaRepository<Schedule, String> {
}






















