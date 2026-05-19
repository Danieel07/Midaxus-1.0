package com.example.midaxus.controller;

import com.example.midaxus.model.dtos.AcademicPeriodDto;
import com.example.midaxus.services.AcademicPeriodService;
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
 * REST Controller for AcademicPeriodController.
 */
@RestController
@RequestMapping("/api/academic-periods")
public class AcademicPeriodController {

  @Autowired
  private AcademicPeriodService service;

  /**
   * Creates a new academic period.
   *
   * @param dto the academic period data transfer object.
   * @return the response entity with the created academic period.
   */
  @PostMapping
  public ResponseEntity<AcademicPeriodDto> create(@RequestBody AcademicPeriodDto dto) {
    AcademicPeriodDto created = service.create(dto);
    return ResponseEntity
        .created(URI.create("/api/academic-periods/" + created.getPeriodId()))
        .body(created);
  }

  /**
   * Gets an academic period by its ID.
   *
   * @param id the ID of the academic period.
   * @return the response entity with the academic period.
   */
  @GetMapping("/{id}")
  public ResponseEntity<AcademicPeriodDto> getById(@PathVariable String id) {
    AcademicPeriodDto result = service.getById(id);
    return ResponseEntity.ok(result);
  }

  /**
   * Gets all academic periods.
   *
   * @return the response entity with the list of all academic periods.
   */
  @GetMapping
  public ResponseEntity<List<AcademicPeriodDto>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  /**
   * Deletes an academic period by its ID.
   *
   * @param id the ID of the academic period to delete.
   * @return the response entity with no content.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}

