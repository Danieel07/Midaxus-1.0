package com.example.midaxus.repositories;

import com.example.midaxus.model.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for TeacherRepository extends JpaRepository<Teacher, String>.
 */
public interface TeacherRepository extends JpaRepository<Teacher, String> {
  Optional<Teacher> findByTeacherCode(String teacherCode);
}






















