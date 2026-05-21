package com.example.midaxus.repositories;

import com.example.midaxus.model.entities.ScheduleSession;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for ScheduleSessionRepository extends JpaRepository<ScheduleSession, String>.
 */
public interface ScheduleSessionRepository extends JpaRepository<ScheduleSession, String> {
  long countByCourseGroup_Teacher_TeacherCode(String teacherCode);
}






















