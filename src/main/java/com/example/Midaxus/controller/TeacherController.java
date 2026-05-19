package com.example.midaxus.controller;

import com.example.midaxus.model.dtos.TeacherDto;
import com.example.midaxus.services.ITeacher;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for managing teachers.
 */
@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

  @Autowired
  private ITeacher<TeacherDto, String> teacherService;

  /**
   * Creates a new teacher.
   *
   * @param dto the teacher DTO to create
   * @return a ResponseEntity containing the created teacher DTO
   */
  @PostMapping
  public ResponseEntity<TeacherDto> createTeacher(@RequestBody TeacherDto dto) {
    TeacherDto done = teacherService.createTeacher(dto);
    return ResponseEntity
      .created(URI.create("/api/teachers/" + done.getTeacherCode()))
      .body(done);
  }

  /**
   * Updates an existing teacher.
   *
   * @param id the ID of the teacher to update
   * @param dto the teacher DTO with updated information
   * @return a ResponseEntity containing the updated teacher DTO
   */
  @PutMapping("/{id}")
  public ResponseEntity<TeacherDto> updateTeacher(
      @PathVariable String id, @RequestBody TeacherDto dto) {
    return ResponseEntity.ok(teacherService.updateTeacher(id, dto));
  }

  /**
   * Retrieves all teachers.
   *
   * @return a ResponseEntity containing a list of teacher DTOs
   */
  @GetMapping
  public ResponseEntity<List<TeacherDto>> getTeachers() {
    return ResponseEntity.ok(teacherService.getTeachers());
  }

  /**
   * Deletes a teacher by their ID.
   *
   * @param id the ID of the teacher to delete
   * @return a ResponseEntity with no content
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTeacher(@PathVariable String id) {
    teacherService.deleteTeacher(id);
    return ResponseEntity.noContent().build();
  }
}

