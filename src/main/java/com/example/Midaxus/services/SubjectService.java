package com.example.midaxus.services;

import com.example.midaxus.model.dtos.SubjectDto;
import com.example.midaxus.model.entities.CourseGroup;
import com.example.midaxus.model.entities.InstitutionPolicy;
import com.example.midaxus.model.entities.Subject;
import com.example.midaxus.model.entities.Teacher;
import com.example.midaxus.model.mapper.SubjectMapper;
import com.example.midaxus.repositories.AttendanceRepository;
import com.example.midaxus.repositories.InstitutionPolicyRepository;
import com.example.midaxus.repositories.SubjectRepository;
import com.example.midaxus.repositories.TeacherRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for managing subjects.
 */
@Service
public class SubjectService implements ISubject<SubjectDto, String> {

  private final SubjectRepository subjectRepository;
  private final InstitutionPolicyRepository policyRepository;
  private final AttendanceRepository attendanceRepository;
  private final TeacherRepository teacherRepository;

  /**
   * Constructs a SubjectService with the specified repositories.
   *
   * @param subjectRepository the repository for subjects
   * @param policyRepository the repository for institution policies
   * @param attendanceRepository the repository for attendance records
   * @param teacherRepository the repository for teachers
   */
  public SubjectService(SubjectRepository subjectRepository,
      InstitutionPolicyRepository policyRepository,
      AttendanceRepository attendanceRepository,
      TeacherRepository teacherRepository) {
    this.subjectRepository = subjectRepository;
    this.policyRepository = policyRepository;
    this.attendanceRepository = attendanceRepository;
    this.teacherRepository = teacherRepository;
  }

  /**
   * Creates a new subject after validating session counts and duration.
   *
   * @param subjectDto the DTO containing subject data
   * @return the saved subject DTO
   */
  @Override
  public SubjectDto create(SubjectDto subjectDto) {
    InstitutionPolicy policy = policyRepository.findById(1L).orElseGet(() -> {
      InstitutionPolicy p = new InstitutionPolicy();
      p.setMaxSessionsPerWeek(3);
      return p;
    });

    // VALIDATION HU-11: Parameterized session range
    int maxSessions = policy.getMaxSessionsPerWeek() != null ? policy.getMaxSessionsPerWeek() : 3;

    if (subjectDto.getSessionPerWeek() < 1 || subjectDto.getSessionPerWeek() > maxSessions) {
      throw new RuntimeException("Sesiones por semana inválidas (1-" + maxSessions + ")");
    }

    if (subjectDto.getDurationMinutes() != 120) {
      throw new RuntimeException("Cada sesión debe durar 120 minutos");
    }

    Subject subject = SubjectMapper.toEntity(subjectDto);
    Subject saved = subjectRepository.save(subject);
    return SubjectMapper.toDto(saved);
  }

  /**
   * Updates an existing subject.
   *
   * @param dto the DTO containing updated subject data
   * @return the updated subject DTO
   */
  public SubjectDto update(SubjectDto dto) {
    Subject subject = subjectRepository.findById(dto.getIdSubject())
        .orElseThrow(() -> new RuntimeException("Subject no encontrado"));

    InstitutionPolicy policy = policyRepository.findById(1L).orElseGet(() -> {
      InstitutionPolicy p = new InstitutionPolicy();
      p.setMaxSessionsPerWeek(3);
      return p;
    });

    int maxSessions = policy.getMaxSessionsPerWeek() != null ? policy.getMaxSessionsPerWeek() : 3;

    if (dto.getSessionPerWeek() < 1 || dto.getSessionPerWeek() > maxSessions) {
      throw new RuntimeException("Sesiones por semana inválidas (1-" + maxSessions + ")");
    }

    subject.setSubjectName(dto.getSubjectName());
    subject.setSessionPerWeek(dto.getSessionPerWeek());
    // durationMinutes stays at 120 (business rule)

    Subject saved = subjectRepository.save(subject);
    return SubjectMapper.toDto(saved);
  }

  /**
   * Deletes a subject by its ID, performing a complete cleanup of dependencies.
   *
   * @param id the ID of the subject to delete
   */
  @Override
  @Transactional
  public void delete(String id) {
    Subject subject = subjectRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Subject no encontrado"));

    // 1. Clean up teacher competencies (ManyToMany)
    if (subject.getTeachers() != null) {
      for (Teacher teacher : subject.getTeachers()) {
        teacher.getHabilitatedSubjects().remove(subject);
        teacherRepository.save(teacher);
      }
    }

    // 2. Clean up attendance records for all groups of this subject
    if (subject.getCourseGroups() != null) {
      for (CourseGroup group : subject.getCourseGroups()) {
        attendanceRepository.deleteByCourseGroupId(group.getCourseGroupId());
      }
    }

    // 3. Delete the subject (CascadeType.ALL in Subject will handle CourseGroups,
    // and CascadeType.ALL in CourseGroup will handle Enrollments, etc.)
    subjectRepository.delete(subject);
  }

  /**
   * Retrieves all subjects.
   *
   * @return a list of subject DTOs
   */
  @Override
  public List<SubjectDto> findAll() {
    return SubjectMapper.toDtoList(subjectRepository.findAll());
  }

  /**
   * Retrieves a subject by its ID.
   *
   * @param id the ID of the subject to retrieve
   * @return the subject DTO
   */
  @Override
  public SubjectDto findById(String id) {
    Subject subject = subjectRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Subject no encontrado"));
    return SubjectMapper.toDto(subject);
  }
}

