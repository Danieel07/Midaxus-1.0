package com.example.midaxus.model.mapper;

import com.example.midaxus.model.dtos.StudentDto;
import com.example.midaxus.model.entities.Student;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class StudentMapper {


  public static StudentDto toDto(Student student){

    if (student == null) {
  return null;
  }

    return new StudentDto(
      student.getId(),
      student.getStudentId(),
      student.getFirstName(),
      student.getLastName(),
      student.getUserName(),
      student.getEmail(),
      student.getPassword(),
      student.getSignInDate()
    );
  }

  public static Student toEntity(StudentDto dto){

    if (dto == null) {
  return null;
  }

    Student student = new Student();

    student.setStudentId(dto.getStudentId());
    student.setFirstName(dto.getFirstName());
    student.setLastName(dto.getLastName());
    student.setUserName(dto.getUserName());
    student.setEmail(dto.getEmail());
    student.setPassword(dto.getPassword());


    return student;
  }

  public static List<StudentDto> toDtolist(List<Student>students){
    if (students == null) {
  return Collections.emptyList();
  }

    return students.stream().map(StudentMapper::toDto).toList();


  }
}






















