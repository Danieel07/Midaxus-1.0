package com.example.midaxus.controller;

import com.example.midaxus.model.dtos.CourseGroupDto;
import com.example.midaxus.model.dtos.EnrollmentDto;
import com.example.midaxus.model.dtos.StudentDto;
import com.example.midaxus.services.CourseGroupService;
import com.example.midaxus.services.EnrollmentService;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for EnrollmentController.
 */
@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

  @Autowired
  private EnrollmentService enrollmentService;

  @Autowired
  private CourseGroupService courseGroupService;

  /**
   * Gets students by course group ID.
   *
   * @param courseGroupId the ID of the course group.
   * @return the response entity with the list of students.
   */
  @GetMapping("/course/{courseGroupId}/students")
  public ResponseEntity<List<StudentDto>> getStudentsByCourse(@PathVariable String courseGroupId) {
    return ResponseEntity.ok(enrollmentService.getStudentsByCourseGroup(courseGroupId));
  }

  /**
   * Creates a new enrollment.
   *
   * @param dto the enrollment data transfer object.
   * @return the response entity with the created enrollment or an error message.
   */
  @PostMapping
  public ResponseEntity<?> create(@RequestBody EnrollmentDto dto) {
    System.out.println("=== LLEGO PETICION DE ENROLLMENT ===");
    System.out.println("StudentId: " + dto.getStudentId());
    System.out.println("CourseGroupId: " + dto.getCourseGroupId());

    try {
      EnrollmentDto created = enrollmentService.createEnrollment(dto);

      return ResponseEntity
          .created(URI.create("/api/enrollments/" + created.getEnrollmentId()))
          .body(created);
    } catch (Throwable e) {
      System.out.println("=== ERROR EN ENROLLMENT ===");
      e.printStackTrace(); // Log in Docker
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * Gets all enrollments.
   *
   * @return the response entity with the list of all enrollments.
   */
  @GetMapping
  public ResponseEntity<List<EnrollmentDto>> getAll() {
    return ResponseEntity.ok(enrollmentService.getAll());
  }

  /**
   * Gets an enrollment by its ID.
   *
   * @param id the ID of the enrollment.
   * @return the response entity with the enrollment.
   */
  @GetMapping("/{id}")
  public ResponseEntity<EnrollmentDto> getById(@PathVariable String id) {
    return ResponseEntity.ok(enrollmentService.getEnrollment(id));
  }

  /**
   * Deletes an enrollment by its ID (soft delete).
   *
   * @param id the ID of the enrollment to delete.
   * @return the response entity with no content.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    enrollmentService.deleteEnrollment(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Gets courses by student ID.
   *
   * @param studentId the ID of the student.
   * @return the response entity with the list of course groups.
   */
  @GetMapping("/student/{studentId}/courses")
  public ResponseEntity<List<CourseGroupDto>> getCoursesByStudent(@PathVariable String studentId) {
    return ResponseEntity.ok(courseGroupService.getCoursesByStudent(studentId));
  }
}

