package com.example.midaxus.repositories;

import com.example.midaxus.model.entities.CourseGroup;
import com.example.midaxus.model.entities.Enrollment;
import com.example.midaxus.model.entities.Student;
import com.example.midaxus.model.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for EnrollmentRepository extends JpaRepository<Enrollment, String>.
 */
public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {
  List<Enrollment> getAllByCourseGroup(CourseGroup courseGroup);

  List<Enrollment> findAllByStatus(EnrollmentStatus status);
  long countByStatus(EnrollmentStatus status);
  boolean existsByStudentAndCourseGroup(Student student, CourseGroup courseGroup);
  List<Enrollment> findByStudent_StudentIdAndStatus(String studentId,EnrollmentStatus status
  );

}






















