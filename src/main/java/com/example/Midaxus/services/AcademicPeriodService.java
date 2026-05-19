package com.example.midaxus.services;

import com.example.midaxus.model.dtos.AcademicPeriodDto;
import com.example.midaxus.model.entities.AcademicPeriod;
import com.example.midaxus.model.mapper.AcademicPeriodMapper;
import com.example.midaxus.repositories.AcademicPeriodRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing academic periods.
 */
@Service
public class AcademicPeriodService implements IAcademicPeriod<AcademicPeriodDto, String> {

  @Autowired
  private AcademicPeriodRepository academicPeriodRepository;

  /**
   * Creates a new academic period.
   *
   * @param dto the data transfer object containing period details
   * @return the created academic period as a DTO
   * @throws RuntimeException if data is invalid or dates are inconsistent
   */
  @Override
  public AcademicPeriodDto create(AcademicPeriodDto dto) {
    if (dto == null) {
      throw new RuntimeException("Datos inválidos");
    }

    if (dto.getStartDate().isAfter(dto.getEndDate())) {
      throw new RuntimeException("La fecha de inicio no puede ser mayor que la de fin");
    }

    if (dto.getEnrollmentStartDate().isAfter(dto.getEnrollmentEndDate())) {
      throw new RuntimeException("Fechas de inscripción inválidas");
    }

    AcademicPeriod entity = AcademicPeriodMapper.toEntity(dto);
    AcademicPeriod saved = academicPeriodRepository.save(entity);

    return AcademicPeriodMapper.toDto(saved);
  }

  /**
   * Retrieves an academic period by its ID.
   *
   * @param id the unique identifier of the period
   * @return the academic period DTO
   * @throws RuntimeException if the period is not found
   */
  @Override
  public AcademicPeriodDto getById(String id) {
    AcademicPeriod entity = academicPeriodRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Periodo no encontrado"));

    return AcademicPeriodMapper.toDto(entity);
  }

  /**
   * Retrieves all academic periods.
   *
   * @return a list of all academic period DTOs
   */
  @Override
  public List<AcademicPeriodDto> getAll() {
    return AcademicPeriodMapper.toDtoList(academicPeriodRepository.findAll());
  }

  /**
   * Deletes an academic period by its ID.
   *
   * @param id the unique identifier of the period to delete
   * @throws RuntimeException if the period does not exist
   */
  @Override
  public void delete(String id) {
    if (!academicPeriodRepository.existsById(id)) {
      throw new RuntimeException("Periodo no existe");
    }

    academicPeriodRepository.deleteById(id);
  }
}

