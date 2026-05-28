package com.example.midaxus.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.midaxus.model.dtos.EnrollmentDto;
import com.example.midaxus.model.entities.CourseGroup;
import com.example.midaxus.model.entities.Enrollment;
import com.example.midaxus.model.entities.InstitutionPolicy;
import com.example.midaxus.model.entities.Student;
import com.example.midaxus.model.entities.Teacher;
import com.example.midaxus.model.entities.Subject;
import com.example.midaxus.model.enums.EnrollmentStatus;
import com.example.midaxus.repositories.CourseGroupRepository;
import com.example.midaxus.repositories.EnrollmentRepository;
import com.example.midaxus.repositories.InstitutionPolicyRepository;
import com.example.midaxus.repositories.StudentRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceTest {

  @Mock
  private EnrollmentRepository enrollmentRepository;

  @Mock
  private StudentRepository studentRepository;

  @Mock
  private CourseGroupRepository courseGroupRepository;

  @Mock
  private InstitutionPolicyRepository policyRepository;

  @InjectMocks
  private EnrollmentService enrollmentService;

  private Student student;
  private CourseGroup courseGroup;
  private InstitutionPolicy policy;

  @BeforeEach
  void setUp() {
    student = new Student();
    student.setId("STU1");
    student.setStudentId("STU1");

    courseGroup = new CourseGroup();
    courseGroup.setCourseGroupId("CG1");
    courseGroup.setTeacher(new Teacher());
    courseGroup.setSubject(new Subject());
    courseGroup.setCapacity(0); // Uses standard

    policy = new InstitutionPolicy();
    policy.setStandardCapacity(40);
    policy.setCapacityTolerancePercent(10);
  }

  @Test
  void createEnrollment_WithinLimit_ShouldSucceed() {
    // Limit = 40 + 10% = 44
    when(studentRepository.findById("STU1")).thenReturn(Optional.of(student));
    when(courseGroupRepository.findById("CG1")).thenReturn(Optional.of(courseGroup));
    when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));
    when(enrollmentRepository.countByCourseGroupAndStatus(courseGroup, EnrollmentStatus.ENROLLED)).thenReturn(43L);
    when(enrollmentRepository.existsByStudentAndCourseGroup(any(), any())).thenReturn(false);
    when(enrollmentRepository.save(any())).thenAnswer(i -> {
      Enrollment e = i.getArgument(0);
      e.setEnrollmentId("E1");
      return e;
    });

    EnrollmentDto dto = new EnrollmentDto();
    dto.setStudentId("STU1");
    dto.setCourseGroupId("CG1");

    EnrollmentDto result = enrollmentService.createEnrollment(dto);
    
    assertTrue(result != null);
  }

  @Test
  void createEnrollment_ExceedingLimit_ShouldThrowException() {
    // Limit = 40 + 10% = 44
    when(studentRepository.findById("STU1")).thenReturn(Optional.of(student));
    when(courseGroupRepository.findById("CG1")).thenReturn(Optional.of(courseGroup));
    when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));
    when(enrollmentRepository.countByCourseGroupAndStatus(courseGroup, EnrollmentStatus.ENROLLED)).thenReturn(44L);
    when(enrollmentRepository.existsByStudentAndCourseGroup(any(), any())).thenReturn(false);

    EnrollmentDto dto = new EnrollmentDto();
    dto.setStudentId("STU1");
    dto.setCourseGroupId("CG1");

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      enrollmentService.createEnrollment(dto);
    });

    assertTrue(exception.getMessage().contains("Curso lleno"));
  }
}
