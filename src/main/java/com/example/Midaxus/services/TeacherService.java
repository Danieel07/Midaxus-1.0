package com.example.midaxus.services;

import com.example.midaxus.model.dtos.TeacherDto;
import com.example.midaxus.model.entities.Subject;
import com.example.midaxus.model.entities.Teacher;
import com.example.midaxus.model.entities.TeacherAvailability;
import com.example.midaxus.model.mapper.TeacherMapper;
import com.example.midaxus.repositories.SubjectRepository;
import com.example.midaxus.repositories.TeacherRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing teachers and their availabilities.
 */
@Service
public class TeacherService implements ITeacher<TeacherDto, String> {

  private final TeacherRepository teacherRepository;
  private final SubjectRepository subjectRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * Constructs a TeacherService with required dependencies.
   *
   * @param teacherRepository the repository for teachers
   * @param subjectRepository the repository for subjects
   * @param passwordEncoder the encoder for teacher passwords
   */
  public TeacherService(
      TeacherRepository teacherRepository,
      SubjectRepository subjectRepository,
      PasswordEncoder passwordEncoder) {
    this.teacherRepository = teacherRepository;
    this.subjectRepository = subjectRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Creates a new teacher, encoding their password and assigning subjects and availabilities.
   *
   * @param teacherDto the DTO containing teacher data
   * @return the saved teacher DTO
   */
  @Override
  public TeacherDto createTeacher(TeacherDto teacherDto) {
    Teacher teacher = TeacherMapper.toEntity(teacherDto);
    if (teacher.getPassword() != null) {
      teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
    }

    // Assign subjects (competencies)
    if (teacherDto.getSubjectsIds() != null) {
      List<Subject> subjects = subjectRepository.findAllById(teacherDto.getSubjectsIds());
      teacher.setHabilitatedSubjects(subjects);
    }

    // Assign availabilities
    if (teacherDto.getAvailabilities() != null) {
      List<TeacherAvailability> availabilities = teacherDto.getAvailabilities().stream().map(dto -> {
        TeacherAvailability availability = new TeacherAvailability();
        availability.setDayOfWeek(dto.getDayOfWeek());
        availability.setStartTime(dto.getStartTime());
        availability.setEndTime(dto.getEndTime());
        availability.setTeacher(teacher);
        return availability;
      }).toList();
      teacher.setAvailabilities(availabilities);
    }

    Teacher saved = teacherRepository.save(teacher);
    return TeacherMapper.toDto(saved);
  }

  /**
   * Updates an existing teacher's subjects and availabilities.
   *
   * @param teacherId the ID/code of the teacher to update
   * @param teacherDto the DTO containing updated teacher data
   * @return the updated teacher DTO
   */
  @Override
  public TeacherDto updateTeacher(String teacherId, TeacherDto teacherDto) {
    Teacher teacher = teacherRepository.findByTeacherCode(teacherId)
        .orElseThrow(() -> new RuntimeException("Teacher no encontrado"));

    // Update subjects (competencies)
    if (teacherDto.getSubjectsIds() != null) {
      List<Subject> subjects = subjectRepository.findAllById(teacherDto.getSubjectsIds());
      teacher.setHabilitatedSubjects(subjects);
    }

    // Update availabilities
    if (teacherDto.getAvailabilities() != null) {
      if (teacher.getAvailabilities() != null) {
        teacher.getAvailabilities().clear();
      }
      List<TeacherAvailability> availabilities = teacherDto.getAvailabilities().stream().map(dto -> {
        TeacherAvailability availability = new TeacherAvailability();
        availability.setDayOfWeek(dto.getDayOfWeek());
        availability.setStartTime(dto.getStartTime());
        availability.setEndTime(dto.getEndTime());
        availability.setTeacher(teacher);
        return availability;
      }).toList();
      if (teacher.getAvailabilities() != null) {
        teacher.getAvailabilities().addAll(availabilities);
      } else {
        teacher.setAvailabilities(availabilities);
      }
    }

    Teacher saved = teacherRepository.save(teacher);
    return TeacherMapper.toDto(saved);
  }

  /**
   * Deletes a teacher by their ID.
   *
   * @param teacherId the ID of the teacher to delete
   */
  @Override
  public void deleteTeacher(String teacherId) {
    teacherRepository.deleteById(teacherId);
  }

  /**
   * Retrieves a teacher by their ID.
   *
   * @param teacherId the ID/code of the teacher to retrieve
   * @return the teacher DTO
   */
  @Override
  public TeacherDto getTeacher(String teacherId) {
    Teacher teacher = teacherRepository.findByTeacherCode(teacherId)
        .orElseThrow(() -> new RuntimeException("Teacher no encontrado"));
    return TeacherMapper.toDto(teacher);
  }

  /**
   * Retrieves all teachers.
   *
   * @return a list of teacher DTOs
   */
  @Override
  public List<TeacherDto> getTeachers() {
    return teacherRepository.findAll()
        .stream()
        .map(TeacherMapper::toDto)
        .toList();
  }
}

