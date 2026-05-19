package com.example.midaxus.controller;

import com.example.midaxus.model.dtos.SubjectDto;
import com.example.midaxus.services.SubjectService;
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
 * REST Controller for managing subjects.
 */
@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

  @Autowired
  private SubjectService subjectService;

  /**
   * Creates a new subject.
   *
   * @param dto the subject DTO to create
   * @return a ResponseEntity containing the created subject DTO
   */
  @PostMapping
  public ResponseEntity<SubjectDto> create(@RequestBody SubjectDto dto) {
    SubjectDto created = subjectService.create(dto);
    return ResponseEntity
      .created(URI.create("/api/subjects/" + created.getIdSubject()))
      .body(created);
  }

  /**
   * Retrieves all subjects.
   *
   * @return a ResponseEntity containing a list of subject DTOs
   */
  @GetMapping
  public ResponseEntity<List<SubjectDto>> findAll() {
    return ResponseEntity.ok(subjectService.findAll());
  }

  /**
   * Retrieves a subject by its ID.
   *
   * @param id the ID of the subject to retrieve
   * @return a ResponseEntity containing the subject DTO
   */
  @GetMapping("/{id}")
  public ResponseEntity<SubjectDto> findById(@PathVariable String id) {
    return ResponseEntity.ok(subjectService.findById(id));
  }

  /**
   * Updates a subject.
   *
   * @param id the ID of the subject to update
   * @param dto the subject DTO with updated information
   * @return a ResponseEntity containing the updated subject DTO
   */
  @PutMapping("/{id}")
  public ResponseEntity<SubjectDto> update(@PathVariable String id, @RequestBody SubjectDto dto) {
    dto.setIdSubject(id);
    SubjectDto updated = subjectService.update(dto);
    return ResponseEntity.ok(updated);
  }

  /**
   * Deletes a subject by its ID.
   *
   * @param id the ID of the subject to delete
   * @return a ResponseEntity with no content
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    subjectService.delete(id);
    return ResponseEntity.noContent().build();
  }
}

