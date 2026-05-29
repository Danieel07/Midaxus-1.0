package com.example.midaxus.services;

import com.example.midaxus.model.dtos.EnrollmentDto;
import com.example.midaxus.model.dtos.StudentDto;
import com.example.midaxus.model.entities.CourseGroup;
import com.example.midaxus.model.entities.Enrollment;
import com.example.midaxus.model.entities.InstitutionPolicy;
import com.example.midaxus.model.entities.Student;
import com.example.midaxus.model.enums.EnrollmentStatus;
import com.example.midaxus.model.mapper.EnrollmentMapper;
import com.example.midaxus.model.mapper.StudentMapper;
import com.example.midaxus.repositories.CourseGroupRepository;
import com.example.midaxus.repositories.EnrollmentRepository;
import com.example.midaxus.repositories.InstitutionPolicyRepository;
import com.example.midaxus.repositories.StudentRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing student enrollments in course groups.
 */
@Service
public class EnrollmentService implements IEnrollment<EnrollmentDto, String> {

  @Autowired
  private EnrollmentRepository enrollmentRepository;

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private CourseGroupRepository courseGroupRepository;

  @Autowired
  private InstitutionPolicyRepository policyRepository;

  /**
   * Retrieves all students enrolled in a specific course group.
   *
   * @param courseGroupId the unique identifier of the course group
   * @return a list of students as DTOs
   * @throws RuntimeException if the course group is not found
   */
  public List<StudentDto> getStudentsByCourseGroup(String courseGroupId) {
    CourseGroup courseGroup = courseGroupRepository.findById(courseGroupId)
        .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

    return enrollmentRepository.getAllByCourseGroup(courseGroup).stream()
        .filter(e -> e.getStatus() == EnrollmentStatus.ENROLLED)
        .map(e -> StudentMapper.toDto(e.getStudent()))
        .toList();
  }

  /**
   * Creates a new enrollment for a student in a course group.
   *
   * @param dto the data transfer object containing enrollment details
   * @return the created enrollment as a DTO
   * @throws RuntimeException if student or course is not found, or if capacity is exceeded
   */
  @Override
  public EnrollmentDto createEnrollment(EnrollmentDto dto) {
    if (dto == null) {
      throw new RuntimeException("Datos inválidos");
    }

    Student student = studentRepository.findById(dto.getStudentId())
        .orElseGet(() -> studentRepository.findAll().stream()
            .filter(s -> (s.getStudentId() != null && s.getStudentId().equals(dto.getStudentId()))
                || (s.getId() != null && s.getId().equals(dto.getStudentId()))
                || (s.getEmail() != null && s.getEmail().equals(dto.getStudentId())))
            .findFirst()
            .orElseThrow(() -> new RuntimeException(
                "Student no encontrado con el ID: " + dto.getStudentId())));

    CourseGroup courseGroup = courseGroupRepository.findById(dto.getCourseGroupId())
        .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

    if (courseGroup.getTeacher() == null || courseGroup.getSubject() == null) {
      throw new RuntimeException("El grupo no tiene un profesor o materia asignada");
    }

    if (enrollmentRepository.existsByStudentAndCourseGroup(student, courseGroup)) {
      throw new RuntimeException("Ya estás inscrito en este curso");
    }

    InstitutionPolicy policy = policyRepository.findById(1L).orElseGet(() -> {
      InstitutionPolicy p = new InstitutionPolicy();
      p.setStandardCapacity(40);
      p.setCapacityTolerancePercent(0);
      return p;
    });

    int stdCap = policy.getStandardCapacity() != null ? policy.getStandardCapacity() : 40;
    int tolPct =
        policy.getCapacityTolerancePercent() != null ? policy.getCapacityTolerancePercent() : 0;

    long currentCount = enrollmentRepository.countByCourseGroupAndStatus(courseGroup, EnrollmentStatus.ENROLLED);
    int baseCapacity = courseGroup.getCapacity() > 0 ? courseGroup.getCapacity() : stdCap;
    int maxAllowed = baseCapacity + (baseCapacity * tolPct / 100);

    if (currentCount >= maxAllowed) {
      throw new RuntimeException("Curso lleno (aforo máximo alcanzado: " + maxAllowed + ")");
    }

    Enrollment enrollment = EnrollmentMapper.toEntity(dto);
    enrollment.setStudent(student);
    enrollment.setCourseGroup(courseGroup);
    enrollment.setStatus(EnrollmentStatus.ENROLLED);

    Enrollment saved = enrollmentRepository.save(enrollment);

    return EnrollmentMapper.toDto(saved);
  }

  /**
   * Retrieves an enrollment by its ID.
   *
   * @param id the unique identifier of the enrollment
   * @return the enrollment DTO
   * @throws RuntimeException if not found
   */
  @Override
  public EnrollmentDto getEnrollment(String id) {
    Enrollment e = enrollmentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("No encontrado"));

    return EnrollmentMapper.toDto(e);
  }

  /**
   * Retrieves all active enrollments.
   *
   * @return a list of enrollment DTOs with ENROLLED status
   */
  @Override
  public List<EnrollmentDto> getAll() {
    return EnrollmentMapper.toDtoList(
        enrollmentRepository.findAllByStatus(EnrollmentStatus.ENROLLED));
  }

  /**
   * Marks an enrollment as DROPPED.
   *
   * @param id the unique identifier of the enrollment to drop
   * @throws RuntimeException if enrollment is not found or already dropped
   */
  @Override
  public void deleteEnrollment(String id) {
    Enrollment enrollment = enrollmentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Enrollment no encontrado"));

    if (enrollment.getStatus() == EnrollmentStatus.DROPPED) {
      throw new RuntimeException("El estudiante ya se retiró de este curso");
    }

    enrollment.setStatus(EnrollmentStatus.DROPPED);
    enrollmentRepository.save(enrollment);
  }
}

