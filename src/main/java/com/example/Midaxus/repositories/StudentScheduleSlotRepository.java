package com.example.midaxus.repositories;

import com.example.midaxus.model.entities.StudentScheduleSlot;
import com.example.midaxus.model.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
/**
 * Repository interface for StudentScheduleSlotRepository extends JpaRepository<StudentScheduleSlot, String>.
 */
public interface StudentScheduleSlotRepository extends JpaRepository<StudentScheduleSlot, String> {
  List<StudentScheduleSlot> findByStudent(Student student);
  void deleteByStudent(Student student);
}






















