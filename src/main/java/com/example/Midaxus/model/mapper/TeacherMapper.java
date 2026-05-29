package com.example.midaxus.model.mapper;

import com.example.midaxus.model.dtos.TeacherDto;
import com.example.midaxus.model.entities.Teacher;

import com.example.midaxus.model.dtos.TeacherAvailabilityDto;
import com.example.midaxus.model.entities.Subject;

import java.util.Collections;
import java.util.List;

public class TeacherMapper {

  public static TeacherDto  toDto(Teacher teacher){
    if (teacher == null) {
  return null;
  }

    TeacherDto dto = new TeacherDto(teacher.getId(),
      teacher.getTeacherCode(),
      teacher.getUserName(),
      teacher.getFirstName(),
      teacher.getLastName(),
      teacher.getEmail(),
      teacher.getPassword());
    
    // Conteo y mapeo seguro para evitar fallos si la DB está desincronizada
    try {
      if (teacher.getHabilitatedSubjects() != null) {
        dto.setSubjectsIds(teacher.getHabilitatedSubjects().stream().map(Subject::getIdSubject).toList());
      }
    } catch (Exception e) {
      dto.setSubjectsIds(Collections.emptyList());
    }
    
    try {
      if (teacher.getAvailabilities() != null) {
        dto.setAvailabilities(teacher.getAvailabilities().stream().map(a -> 
          new TeacherAvailabilityDto(a.getDayOfWeek(), a.getStartTime(), a.getEndTime())
        ).toList());
      }
    } catch (Exception e) {
      dto.setAvailabilities(Collections.emptyList());
    }
    
    return dto;
  }
  public static Teacher toEntity(TeacherDto dto){
    if (dto == null) {
  return null;
  }

    Teacher teacher = new Teacher();
    teacher.setTeacherCode(dto.getTeacherCode());
    teacher.setUserName(dto.getUserName());
    teacher.setFirstName(dto.getFirstName());
    teacher.setLastName(dto.getLastName());
    teacher.setEmail(dto.getEmail());
    teacher.setPassword(dto.getPassword());

    return teacher;

  }

  public static List<TeacherDto> toDtolist(List<Teacher>teachers){
    if (teachers == null) {
  return Collections.emptyList();
  }

    return teachers.stream().map(TeacherMapper::toDto).toList();


  }
}






















