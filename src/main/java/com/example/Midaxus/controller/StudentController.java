package com.example.midaxus.controller;

import com.example.midaxus.model.dtos.StudentDto;
import com.example.midaxus.services.IStudent;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for managing students.
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

  @Autowired
  private IStudent<String, StudentDto> studentService;

  /**
   * Creates a new student.
   *
   * @param dto the student DTO to create
   * @return a ResponseEntity containing the created student DTO
   */
  @PostMapping
  public ResponseEntity<StudentDto> createStudent(@RequestBody StudentDto dto) {
    StudentDto created = studentService.createStudent(dto);
    return ResponseEntity
      .created(URI.create("/api/students/" + created.getStudentId()))
      .body(created);
  }

  /**
   * Retrieves all students.
   *
   * @return a ResponseEntity containing a list of student DTOs
   */
  @GetMapping
  public ResponseEntity<List<StudentDto>> getStudents() {
    return ResponseEntity.ok(studentService.getStudents());
  }

  /**
   * Retrieves a student by their ID.
   *
   * @param id the ID of the student to retrieve
   * @return a ResponseEntity containing the student DTO
   */
  @GetMapping("/{id}")
  public ResponseEntity<StudentDto> getStudent(@PathVariable String id) {
    return ResponseEntity.ok(studentService.getStudent(id));
  }
}

