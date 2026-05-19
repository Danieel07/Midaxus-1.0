package com.example.midaxus.services;

import com.example.midaxus.model.dtos.CourseGroupDto;
import com.example.midaxus.model.entities.AcademicPeriod;
import com.example.midaxus.model.entities.CourseGroup;
import com.example.midaxus.model.entities.Enrollment;
import com.example.midaxus.model.entities.Student;
import com.example.midaxus.model.entities.Subject;
import com.example.midaxus.model.entities.Teacher;
import com.example.midaxus.model.enums.EnrollmentStatus;
import com.example.midaxus.model.mapper.CourseGroupMapper;
import com.example.midaxus.repositories.AcademicPeriodRepository;
import com.example.midaxus.repositories.CourseGroupRepository;
import com.example.midaxus.repositories.EnrollmentRepository;
import com.example.midaxus.repositories.StudentRepository;
import com.example.midaxus.repositories.SubjectRepository;
import com.example.midaxus.repositories.TeacherRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for managing course groups.
 */
@Service
@Transactional
public class CourseGroupService implements ICourseGroup<CourseGroupDto, String> {

  @Autowired
  private CourseGroupRepository courseGroupRepository;

  @Autowired
  private TeacherRepository teacherRepository;

  @Autowired
  private SubjectRepository subjectRepository;

  @Autowired
  private AcademicPeriodRepository academicPeriodRepository;

  @Autowired
  private EnrollmentRepository enrollmentRepository;

  @Autowired
  private StudentRepository studentRepository;

  /**
   * Creates a new course group.
   *
   * @param dto the data transfer object for the course group
   * @return the created course group DTO
   * @throws RuntimeException if dependencies are not found
   */
  @Override
  public CourseGroupDto create(CourseGroupDto dto) {
    if (dto == null) {
      throw new RuntimeException("Datos inválidos");
    }

    Teacher teacher = teacherRepository.findByTeacherCode(dto.getTeacherId())
        .orElseGet(() -> teacherRepository.findById(dto.getTeacherId())
            .orElseThrow(
                () -> new RuntimeException("Profesor no encontrado: " + dto.getTeacherId())));

    Subject subject = subjectRepository.findById(dto.getSubjectId())
        .orElseThrow(() -> new RuntimeException("Materia no encontrada: " + dto.getSubjectId()));

    AcademicPeriod period = null;
    if (dto.getAcademicPeriodId() != null && !dto.getAcademicPeriodId().isEmpty()) {
      period = academicPeriodRepository.findById(dto.getAcademicPeriodId())
          .orElseThrow(() -> new RuntimeException("Periodo no encontrado"));
    }

    CourseGroup entity = CourseGroupMapper.toEntity(dto);
    if (entity.getCourseGroupId() == null || entity.getCourseGroupId().isEmpty()) {
      entity.setCourseGroupId(UUID.randomUUID().toString());
    }

    entity.setTeacher(teacher);
    entity.setSubject(subject);
    entity.setAcademicPeriod(period);

    CourseGroup saved = courseGroupRepository.save(entity);

    return CourseGroupMapper.toDto(saved);
  }

  /**
   * Updates an existing course group.
   *
   * @param id the unique identifier of the course group
   * @param dto the data transfer object with updated details
   * @return the updated course group DTO
   * @throws RuntimeException if the course group or dependencies are not found
   */
  @Override
  public CourseGroupDto update(String id, CourseGroupDto dto) {
    if (dto == null) {
      throw new RuntimeException("Datos inválidos");
    }

    CourseGroup existing = courseGroupRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("CourseGroup no encontrado"));

    Teacher teacher = existing.getTeacher();
    if (dto.getTeacherId() != null && !dto.getTeacherId().isEmpty()) {
      teacher = teacherRepository.findByTeacherCode(dto.getTeacherId())
          .orElseGet(() -> teacherRepository.findById(dto.getTeacherId())
              .orElseThrow(() -> new RuntimeException("Teacher no encontrado")));
    }

    Subject subject = existing.getSubject();
    if (dto.getSubjectId() != null && !dto.getSubjectId().isEmpty()) {
      subject = subjectRepository.findById(dto.getSubjectId())
          .orElseThrow(() -> new RuntimeException("Subject no encontrado"));
    }

    AcademicPeriod period = existing.getAcademicPeriod();
    if (dto.getAcademicPeriodId() != null && !dto.getAcademicPeriodId().isEmpty()) {
      period = academicPeriodRepository.findById(dto.getAcademicPeriodId())
          .orElse(existing.getAcademicPeriod());
    }

    existing.setTeacher(teacher);
    existing.setSubject(subject);
    existing.setAcademicPeriod(period);
    existing.setCapacity(dto.getCapacity());
    existing.setCode(dto.getCode());

    CourseGroup updated = courseGroupRepository.save(existing);
    return CourseGroupMapper.toDto(updated);
  }

  /**
   * Retrieves a course group by its ID.
   *
   * @param id the unique identifier of the course group
   * @return the course group DTO
   * @throws RuntimeException if the course group is not found
   */
  @Override
  public CourseGroupDto getById(String id) {
    CourseGroup cg = courseGroupRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("CourseGroup no encontrado"));

    return CourseGroupMapper.toDto(cg);
  }

  /**
   * Retrieves all course groups.
   *
   * @return a list of all course group DTOs
   */
  @Override
  public List<CourseGroupDto> getAll() {
    return CourseGroupMapper.toDtoList(courseGroupRepository.findAll());
  }

  /**
   * Deletes a course group by its ID.
   *
   * @param id the unique identifier of the course group to delete
   * @throws RuntimeException if the course group does not exist
   */
  @Override
  public void delete(String id) {
    if (!courseGroupRepository.existsById(id)) {
      throw new RuntimeException("CourseGroup no existe");
    }

    courseGroupRepository.deleteById(id);
  }

  /**
   * Retrieves course groups assigned to a specific teacher.
   *
   * @param teacherId the ID of the teacher
   * @return a list of course group DTOs
   * @throws RuntimeException if the teacher is not found
   */
  @Override
  public List<CourseGroupDto> getByTeacher(String teacherId) {
    Teacher teacher = teacherRepository.findById(teacherId)
        .orElseThrow(() -> new RuntimeException("Teacher no encontrado"));

    return CourseGroupMapper.toDtoList(courseGroupRepository.findAllByTeacher(teacher));
  }

  /**
   * Retrieves course groups for a specific subject.
   *
   * @param subjectId the ID of the subject
   * @return a list of course group DTOs
   * @throws RuntimeException if the subject is not found
   */
  @Override
  public List<CourseGroupDto> getBySubject(String subjectId) {
    Subject subject = subjectRepository.findById(subjectId)
        .orElseThrow(() -> new RuntimeException("Subject no encontrado"));

    return CourseGroupMapper.toDtoList(courseGroupRepository.findAllBySubject(subject));
  }

  /**
   * Retrieves courses assigned to a teacher by their teacher code.
   *
   * @param teacherId the teacher code
   * @return a list of course group DTOs
   */
  @Override
  public List<CourseGroupDto> getCoursesByTeacher(String teacherId) {
    return CourseGroupMapper.toDtoList(courseGroupRepository.findByTeacher_TeacherCode(teacherId));
  }

  /**
   * Retrieves courses a student is enrolled in.
   *
   * @param studentId the student identifier (ID, UUID, or email)
   * @return a list of course group DTOs
   */
  @Override
  public List<CourseGroupDto> getCoursesByStudent(String studentId) {
    // Intentar buscar por studentId primero
    List<Enrollment> enrollments = enrollmentRepository
        .findByStudent_StudentIdAndStatus(studentId, EnrollmentStatus.ENROLLED);

    // Si no encontró nada, intentar buscar al estudiante por su UUID (id de User)
    if (enrollments.isEmpty()) {
      Student student = studentRepository.findById(studentId).orElse(null);
      if (student != null && student.getStudentId() != null) {
        enrollments = enrollmentRepository
            .findByStudent_StudentIdAndStatus(student.getStudentId(), EnrollmentStatus.ENROLLED);
      }
    }

    // Si aún no encontró nada, intentar buscar por email (fallback final)
    if (enrollments.isEmpty()) {
      Student student = studentRepository.findAll().stream()
          .filter(s -> s.getEmail() != null && s.getEmail().equals(studentId))
          .findFirst()
          .orElse(null);
      if (student != null && student.getStudentId() != null) {
        enrollments = enrollmentRepository
            .findByStudent_StudentIdAndStatus(student.getStudentId(), EnrollmentStatus.ENROLLED);
      }
    }

    return enrollments.stream()
        .map(Enrollment::getCourseGroup)
        .map(CourseGroupMapper::toDto)
        .toList();
  }
}

