package com.example.midaxus.controller;

import com.example.midaxus.model.dtos.CourseGroupDto;
import com.example.midaxus.services.CourseGroupService;
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
 * REST Controller for CourseGroupController.
 */
@RestController
@RequestMapping("/api/course-groups")
public class CourseGroupController {

  @Autowired
  private CourseGroupService service;

  /**
   * Creates a new course group.
   *
   * @param dto the course group data transfer object.
   * @return the response entity with the created course group.
   */
  @PostMapping
  public ResponseEntity<CourseGroupDto> create(@RequestBody CourseGroupDto dto) {
    CourseGroupDto created = service.create(dto);
    return ResponseEntity
        .created(URI.create("/api/course-groups/" + created.getCourseGroupId()))
        .body(created);
  }

  /**
   * Gets course groups by teacher ID.
   *
   * @param teacherId the ID of the teacher.
   * @return the response entity with the list of course groups.
   */
  @GetMapping("/teacher/{teacherId}")
  public ResponseEntity<List<CourseGroupDto>> getByTeacher(@PathVariable String teacherId) {
    return ResponseEntity.ok(service.getCoursesByTeacher(teacherId));
  }

  /**
   * Updates an existing course group.
   *
   * @param id the ID of the course group to update.
   * @param dto the updated course group data transfer object.
   * @return the response entity with the updated course group.
   */
  @PutMapping("/{id}")
  public ResponseEntity<CourseGroupDto> update(@PathVariable String id,
      @RequestBody CourseGroupDto dto) {
    CourseGroupDto updated = service.update(id, dto);
    return ResponseEntity.ok(updated);
  }

  /**
   * Gets a course group by its ID.
   *
   * @param id the ID of the course group.
   * @return the response entity with the course group.
   */
  @GetMapping("/{id}")
  public ResponseEntity<CourseGroupDto> getById(@PathVariable String id) {
    return ResponseEntity.ok(service.getById(id));
  }

  /**
   * Gets all course groups.
   *
   * @return the response entity with the list of all course groups.
   */
  @GetMapping
  public ResponseEntity<List<CourseGroupDto>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  /**
   * Deletes a course group by its ID.
   *
   * @param id the ID of the course group to delete.
   * @return the response entity with no content.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Gets course groups by subject ID.
   *
   * @param subjectId the ID of the subject.
   * @return the response entity with the list of course groups.
   */
  @GetMapping("/subject/{subjectId}")
  public ResponseEntity<List<CourseGroupDto>> getBySubject(@PathVariable String subjectId) {
    return ResponseEntity.ok(service.getBySubject(subjectId));
  }

  /**
   * Retrieves course groups that are at risk of closure due to low enrollment.
   *
   * @return a list of course groups at risk
   */
  @GetMapping("/at-risk")
  public ResponseEntity<List<CourseGroupDto>> getGroupsAtRisk() {
    return ResponseEntity.ok(service.getGroupsAtRisk());
  }

  /**
   * Closes a course group.
   *
   * @param id the unique identifier of the course group to close
   * @return the updated course group DTO
   */
  @PostMapping("/{id}/close")
  public ResponseEntity<CourseGroupDto> closeGroup(@PathVariable String id) {
    return ResponseEntity.ok(service.closeGroup(id));
  }
}

