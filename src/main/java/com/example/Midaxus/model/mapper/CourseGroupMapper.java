package com.example.midaxus.model.mapper;

import com.example.midaxus.model.dtos.CourseGroupDto;
import com.example.midaxus.model.entities.CourseGroup;

import java.util.List;

public class CourseGroupMapper {


  public static CourseGroupDto toDto(CourseGroup entity) {

    if (entity == null) {
  return null;
  }

    return new CourseGroupDto(
      entity.getCourseGroupId(),
      entity.getTeacher() != null ? entity.getTeacher().getId() : null,
      entity.getSubject() != null ? entity.getSubject().getIdSubject() : null,
      entity.getAcademicPeriod() != null ? entity.getAcademicPeriod().getPeriodId() : null,
      entity.getCode(),
      entity.getCapacity()
    );
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






















