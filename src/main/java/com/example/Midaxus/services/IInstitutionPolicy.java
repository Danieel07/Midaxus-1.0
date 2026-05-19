package com.example.midaxus.services;

import com.example.midaxus.model.dtos.InstitutionPolicyDto;

/**
 * Interface for Institution Policy service operations.
 */
public interface IInstitutionPolicy {

  /**
   * Retrieves the institution policy.
   *
   * @return the institution policy DTO
   */
  InstitutionPolicyDto getPolicy();

  /**
   * Updates the institution policy.
   *
   * @param dto the updated institution policy data
   * @return the updated institution policy DTO
   */
  InstitutionPolicyDto updatePolicy(InstitutionPolicyDto dto);
}

