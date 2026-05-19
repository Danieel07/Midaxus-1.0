package com.example.midaxus.services;

import com.example.midaxus.model.dtos.StudentDto;
import com.example.midaxus.model.entities.Student;
import com.example.midaxus.model.mapper.StudentMapper;
import com.example.midaxus.repositories.StudentRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing students.
 */
@Service
public class StudentService implements IStudent<String, StudentDto> {

  private final StudentRepository studentRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * Constructs a StudentService with required dependencies.
   *
   * @param studentRepository the repository for students
   * @param passwordEncoder the encoder for student passwords
   */
  public StudentService(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
    this.studentRepository = studentRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Creates a new student and encodes their password.
   *
   * @param studentDto the DTO containing student data
   * @return the saved student DTO
   */
  @Override
  public StudentDto createStudent(StudentDto studentDto) {
    Student student = StudentMapper.toEntity(studentDto);
    if (student.getPassword() != null) {
      student.setPassword(passwordEncoder.encode(student.getPassword()));
    }
    Student saved = studentRepository.save(student);
    return StudentMapper.toDto(saved);
  }

  /**
   * Retrieves a student by their ID.
   *
   * @param s the ID of the student to retrieve
   * @return the student DTO
   */
  @Override
  public StudentDto getStudent(String s) {
    Student student = studentRepository.getReferenceById(s);
    return StudentMapper.toDto(student);
  }

  /**
   * Deletes a student.
   *
   * @param studentDto the DTO of the student to delete
   */
  @Override
  public void deleteStudent(StudentDto studentDto) {
    studentRepository.delete(StudentMapper.toEntity(studentDto));
  }

  /**
   * Retrieves all students.
   *
   * @return a list of student DTOs
   */
  @Override
  public List<StudentDto> getStudents() {
    return studentRepository.findAll().stream()
        .map(StudentMapper::toDto)
        .toList();
  }
}

