package com.example.midaxus.services;

import com.example.midaxus.model.dtos.InstitutionPolicyDto;
import com.example.midaxus.model.entities.InstitutionPolicy;
import com.example.midaxus.repositories.InstitutionPolicyRepository;
import java.time.LocalTime;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing institution policies.
 */
@Service
public class InstitutionPolicyService implements IInstitutionPolicy {

  private final InstitutionPolicyRepository repository;

  /**
   * Constructs an InstitutionPolicyService with the specified repository.
   *
   * @param repository the repository for institution policies
   */
  public InstitutionPolicyService(InstitutionPolicyRepository repository) {
    this.repository = repository;
  }

  /**
   * Retrieves the current institution policy, or creates a default one if none exists.
   *
   * @return the institution policy DTO
   */
  @Override
  public InstitutionPolicyDto getPolicy() {
    InstitutionPolicy policy = repository.findById(1L).orElseGet(() -> {
      InstitutionPolicy defaultPolicy = new InstitutionPolicy();
      defaultPolicy.setClassStartTime(LocalTime.of(8, 0));
      defaultPolicy.setClassEndTime(LocalTime.of(18, 0));
      defaultPolicy.setLunchStartTime(LocalTime.of(12, 0));
      defaultPolicy.setLunchEndTime(LocalTime.of(13, 30));
      defaultPolicy.setStandardCapacity(40);
      defaultPolicy.setCapacityTolerancePercent(10);
      defaultPolicy.setMaxSessionsPerWeek(3);
      return repository.save(defaultPolicy);
    });
    return toDto(policy);
  }

  /**
   * Updates the current institution policy with the values provided in the DTO.
   *
   * @param dto the DTO containing the updated policy values
   * @return the updated institution policy DTO
   */
  @Override
  public InstitutionPolicyDto updatePolicy(InstitutionPolicyDto dto) {
    InstitutionPolicy policy = repository.findById(1L).orElse(new InstitutionPolicy());

    if (dto.getClassStartTime() != null) {
      policy.setClassStartTime(dto.getClassStartTime());
    }
    if (dto.getClassEndTime() != null) {
      policy.setClassEndTime(dto.getClassEndTime());
    }
    if (dto.getLunchStartTime() != null) {
      policy.setLunchStartTime(dto.getLunchStartTime());
    }
    if (dto.getLunchEndTime() != null) {
      policy.setLunchEndTime(dto.getLunchEndTime());
    }
    if (dto.getStandardCapacity() != null) {
      if (dto.getStandardCapacity() <= 0) {
        throw new RuntimeException("El aforo estándar debe ser mayor a 0");
      }
      policy.setStandardCapacity(dto.getStandardCapacity());
    }
    if (dto.getCapacityTolerancePercent() != null) {
      if (dto.getCapacityTolerancePercent() < 0) {
        throw new RuntimeException("La tolerancia no puede ser negativa");
      }
      policy.setCapacityTolerancePercent(dto.getCapacityTolerancePercent());
    }
    if (dto.getMaxSessionsPerWeek() != null) {
      policy.setMaxSessionsPerWeek(dto.getMaxSessionsPerWeek());
    }
    if (dto.getMinEnrollmentThreshold() != null) {
      if (dto.getMinEnrollmentThreshold() < 0) {
        throw new RuntimeException("El umbral mínimo no puede ser negativo");
      }
      policy.setMinEnrollmentThreshold(dto.getMinEnrollmentThreshold());
    }

    policy = repository.save(policy);
    return toDto(policy);
  }

  /**
   * Converts an InstitutionPolicy entity to an InstitutionPolicyDto.
   *
   * @param entity the entity to convert
   * @return the corresponding DTO
   */
  private InstitutionPolicyDto toDto(InstitutionPolicy entity) {
    InstitutionPolicyDto dto = new InstitutionPolicyDto();
    dto.setId(entity.getId());
    dto.setClassStartTime(entity.getClassStartTime());
    dto.setClassEndTime(entity.getClassEndTime());
    dto.setLunchStartTime(entity.getLunchStartTime());
    dto.setLunchEndTime(entity.getLunchEndTime());
    dto.setStandardCapacity(entity.getStandardCapacity());
    dto.setCapacityTolerancePercent(entity.getCapacityTolerancePercent());
    dto.setMaxSessionsPerWeek(entity.getMaxSessionsPerWeek());
    dto.setMinEnrollmentThreshold(entity.getMinEnrollmentThreshold());
    return dto;
  }
}

