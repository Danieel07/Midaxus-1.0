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
    dto.setTeacherId(entity.getTeacher() != null ? entity.getTeacher().getId() : null);
    dto.setSubjectId(entity.getSubject() != null ? entity.getSubject().getIdSubject() : null);
    dto.setAcademicPeriodId(entity.getAcademicPeriod() != null ? entity.getAcademicPeriod().getPeriodId() : null);
    dto.setCode(entity.getCode());
    dto.setCapacity(entity.getCapacity());
    dto.setEnrolledCount(0);
    
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






















