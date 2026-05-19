package com.example.midaxus.controller;

import com.example.midaxus.model.dtos.InstitutionPolicyDto;
import com.example.midaxus.services.IInstitutionPolicy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for managing institution policies.
 */
@RestController
@RequestMapping("/api/policies")
public class InstitutionPolicyController {

  private final IInstitutionPolicy service;

  /**
   * Constructs an InstitutionPolicyController with the specified service.
   *
   * @param service the service used to manage institution policies
   */
  public InstitutionPolicyController(IInstitutionPolicy service) {
    this.service = service;
  }

  /**
   * Retrieves the current institution policy.
   *
   * @return a ResponseEntity containing the institution policy DTO
   */
  @GetMapping
  public ResponseEntity<InstitutionPolicyDto> getPolicy() {
    return ResponseEntity.ok(service.getPolicy());
  }

  /**
   * Updates the institution policy.
   *
   * @param dto the institution policy DTO to update
   * @return a ResponseEntity containing the updated institution policy DTO
   */
  @PutMapping
  public ResponseEntity<InstitutionPolicyDto> updatePolicy(@RequestBody InstitutionPolicyDto dto) {
    return ResponseEntity.ok(service.updatePolicy(dto));
  }
}

