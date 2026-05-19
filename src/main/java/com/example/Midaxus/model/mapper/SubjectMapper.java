package com.example.midaxus.model.mapper;

import com.example.midaxus.model.dtos.SubjectDto;
import com.example.midaxus.model.entities.Subject;

import java.util.Collections;
import java.util.List;

public class SubjectMapper {
  public static SubjectDto toDto(Subject subject) {

    if (subject == null) {
  return null;
  }

    return new SubjectDto(
      subject.getIdSubject(),
      subject.getSubjectName(),
      subject.getSessionPerWeek(),
      subject.getDurationMinutes()
    );
  }

  // 🔹 dto → ENTITY
  public static Subject toEntity(SubjectDto dto) {

    if (dto == null) {
  return null;
  }

    Subject subject = new Subject();

    subject.setIdSubject(dto.getIdSubject());
    subject.setSubjectName(dto.getSubjectName());
    subject.setSessionPerWeek(dto.getSessionPerWeek());
    subject.setDurationMinutes(dto.getDurationMinutes());

    return subject;
  }

  // 🔹 LIST → dto LIST
  public static List<SubjectDto> toDtoList(List<Subject> subjects) {

    if (subjects == null) {
  return Collections.emptyList();
  }

    return subjects.stream()
      .map(SubjectMapper::toDto)
      .toList();
  }

}






















