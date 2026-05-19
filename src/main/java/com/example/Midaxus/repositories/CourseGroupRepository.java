package com.example.midaxus.repositories;

import com.example.midaxus.model.entities.CourseGroup;
import com.example.midaxus.model.entities.Subject;
import com.example.midaxus.model.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for CourseGroupRepository extends JpaRepository<CourseGroup, String>.
 */
public interface CourseGroupRepository extends JpaRepository<CourseGroup, String> {
  List<CourseGroup> findAllByTeacher(Teacher teacher);
  List<CourseGroup> findAllBySubject(Subject subject);
  List<CourseGroup> findByTeacher_TeacherCode(String teacherId);
}






















