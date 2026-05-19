package com.example.midaxus.model.mapper;

import com.example.midaxus.model.dtos.EnrollmentDto;
import com.example.midaxus.model.entities.CourseGroup;
import com.example.midaxus.model.entities.Enrollment;
import com.example.midaxus.model.entities.Student;

import java.util.List;

public class EnrollmentMapper {


  public static EnrollmentDto toDto(Enrollment enrollment){
    if (enrollment == null) {
  return null;
  }

    return new EnrollmentDto(
      enrollment.getEnrollmentId(),
      enrollment.getStudent().getStudentId(),
      enrollment.getCourseGroup().getCourseGroupId(),
      enrollment.getStatus()
    );
  }


  public static Enrollment toEntity(EnrollmentDto dto){
    if (dto == null) {
  return null;
  }

    Enrollment enrollment = new Enrollment();
    enrollment.setEnrollmentId(dto.getEnrollmentId());
    enrollment.setStatus(dto.getStatus());

    return enrollment;
  }


  public static List<EnrollmentDto> toDtoList(List<Enrollment> enrollments){
    if (enrollments == null) {
  return List.of();
  }

    return enrollments.stream()
      .map(EnrollmentMapper::toDto)
      .toList();
  }
}






















