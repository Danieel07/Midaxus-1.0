package com.example.midaxus.model.mapper;

import com.example.midaxus.model.dtos.CourseGroupDto;
import com.example.midaxus.model.entities.CourseGroup;
import com.example.midaxus.model.enums.EnrollmentStatus;

import java.util.List;

public class CourseGroupMapper {


  public static CourseGroupDto toDto(CourseGroup entity) {
    if (entity == null) {
      return null;
    }

    CourseGroupDto dto = new CourseGroupDto();
    dto.setCourseGroupId(entity.getCourseGroupId());
    
    // Mapeo seguro de relaciones para evitar fallos en cascada
    try {
      if (entity.getTeacher() != null) {
        dto.setTeacherId(entity.getTeacher().getId());
      }
    } catch (Exception e) {
      dto.setTeacherId(null);
    }
    
    try {
      if (entity.getSubject() != null) {
        dto.setSubjectId(entity.getSubject().getIdSubject());
      }
    } catch (Exception e) {
      dto.setSubjectId(null);
    }
    
    try {
      if (entity.getAcademicPeriod() != null) {
        dto.setAcademicPeriodId(entity.getAcademicPeriod().getPeriodId());
      }
    } catch (Exception e) {
      dto.setAcademicPeriodId(null);
    }
    
    dto.setCode(entity.getCode());
    dto.setCapacity(entity.getCapacity());
    
    // Cálculo dinámico de inscritos activos
    int count = 0;
    try {
      if (entity.getEnrollments() != null) {
        count = (int) entity.getEnrollments().stream()
            .filter(e -> e != null && e.getStatus() == EnrollmentStatus.ENROLLED)
            .count();
      }
    } catch (Exception e) {
      count = 0; // Fallback por si la colección no está cargada o hay error de Lazy
    }
    dto.setEnrolledCount(count);
    
    return dto;
  }


  public static CourseGroup toEntity(CourseGroupDto dto) {

    if (dto == null) {
  return null;
  }

    CourseGroup entity = new CourseGroup();

    entity.setCourseGroupId(dto.getCourseGroupId());
    entity.setCode(dto.getCode());
    entity.setCapacity(dto.getCapacity());

    return entity;
  }


  public static List<CourseGroupDto> toDtoList(List<CourseGroup> list) {

    if (list == null) {
  return List.of();
  }

    return list.stream()
      .map(CourseGroupMapper::toDto)
      .toList();
  }
}






















