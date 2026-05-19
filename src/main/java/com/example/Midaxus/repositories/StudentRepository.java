package com.example.midaxus.repositories;

import com.example.midaxus.model.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for StudentRepository extends JpaRepository<Student, String>.
 */
public interface StudentRepository extends JpaRepository<Student, String> {
  Optional<Student> findByStudentId(String studentId);
  Optional<Student> findByEmail(String email);
}






















