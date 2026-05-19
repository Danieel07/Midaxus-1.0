package com.example.midaxus.model.mapper;

import com.example.midaxus.model.dtos.AcademicPeriodDto;
import com.example.midaxus.model.entities.AcademicPeriod;

import java.util.List;

public class AcademicPeriodMapper {


  public static AcademicPeriodDto toDto(AcademicPeriod entity) {

    if (entity == null) {
  return null;
  }

    return new AcademicPeriodDto(
      entity.getPeriodId(),
      entity.getCode(),
      entity.getDescription(),
      entity.getStartDate(),
      entity.getEndDate(),
      entity.getEnrollmentStartDate(),
      entity.getEnrollmentEndDate()
    );
  }


  public static AcademicPeriod toEntity(AcademicPeriodDto dto) {

    if (dto == null) {
  return null;
  }

    AcademicPeriod entity = new AcademicPeriod();

    entity.setPeriodId(dto.getPeriodId());
    entity.setCode(dto.getCode());
    entity.setDescription(dto.getDescription());
    entity.setStartDate(dto.getStartDate());
    entity.setEndDate(dto.getEndDate());
    entity.setEnrollmentStartDate(dto.getEnrollmentStartDate());
    entity.setEnrollmentEndDate(dto.getEnrollmentEndDate());

    return entity;
  }


  public static List<AcademicPeriodDto> toDtoList(List<AcademicPeriod> list) {

    if (list == null) {
  return List.of();
  }

    return list.stream()
      .map(AcademicPeriodMapper::toDto)
      .toList();
  }
}






















