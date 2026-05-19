package com.example.midaxus.services;

import com.example.midaxus.model.dtos.StudentScheduleSlotDto;
import com.example.midaxus.model.entities.CourseGroup;
import com.example.midaxus.model.entities.Student;
import com.example.midaxus.model.entities.StudentScheduleSlot;
import com.example.midaxus.model.entities.Subject;
import com.example.midaxus.repositories.CourseGroupRepository;
import com.example.midaxus.repositories.StudentRepository;
import com.example.midaxus.repositories.StudentScheduleSlotRepository;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing student schedules.
 */
@Service
public class StudentScheduleService {

  private final StudentScheduleSlotRepository scheduleRepository;
  private final StudentRepository studentRepository;
  private final CourseGroupRepository courseGroupRepository;

  /**
   * Constructs a StudentScheduleService with required repositories.
   *
   * @param scheduleRepository the repository for schedule slots
   * @param studentRepository the repository for students
   * @param courseGroupRepository the repository for course groups
   */
  public StudentScheduleService(
      StudentScheduleSlotRepository scheduleRepository,
      StudentRepository studentRepository,
      CourseGroupRepository courseGroupRepository) {
    this.scheduleRepository = scheduleRepository;
    this.studentRepository = studentRepository;
    this.courseGroupRepository = courseGroupRepository;
  }

  /**
   * Saves the schedule for a specific student.
   *
   * @param studentId the ID of the student
   * @param slots the list of schedule slots to save
   */
  @Transactional
  public void saveStudentSchedule(String studentId, List<StudentScheduleSlotDto> slots) {
    Student student = findStudentFlexible(studentId);

    // Delete previous schedule
    scheduleRepository.deleteByStudent(student);

    if (slots == null || slots.isEmpty()) {
      return;
    }

    List<StudentScheduleSlot> entities = new ArrayList<>();
    for (StudentScheduleSlotDto dto : slots) {
      CourseGroup cg = courseGroupRepository.findById(dto.getCourseGroupId())
          .orElseThrow(() -> new RuntimeException("Grupo de curso no encontrado"));

      DayOfWeek day;
      try {
        day = DayOfWeek.valueOf(dto.getDay().toUpperCase());
      } catch (Exception e) {
        // If it fails to parse, try to handle Spanish days or just continue
        continue;
      }

      StudentScheduleSlot entity = new StudentScheduleSlot(student, cg, day, dto.getSlot());
      entities.add(entity);
    }

    scheduleRepository.saveAll(entities);
  }

  /**
   * Retrieves the schedule for a specific student.
   *
   * @param studentId the ID of the student
   * @return a list of schedule slot DTOs
   */
  @Transactional(readOnly = true)
  public List<StudentScheduleSlotDto> getStudentSchedule(String studentId) {
    Student student = findStudentFlexible(studentId);
    List<StudentScheduleSlot> entities = scheduleRepository.findByStudent(student);
    List<StudentScheduleSlotDto> dtos = new ArrayList<>();

    for (StudentScheduleSlot entity : entities) {
      StudentScheduleSlotDto dto = new StudentScheduleSlotDto();
      dto.setCourseGroupId(entity.getCourseGroup().getCourseGroupId());
      dto.setCourseCode(entity.getCourseGroup().getCode());

      Subject subject = entity.getCourseGroup().getSubject();
      dto.setSubjectName(subject != null ? subject.getSubjectName()
          : entity.getCourseGroup().getCode());

      dto.setDay(entity.getDay().name());
      dto.setSlot(entity.getTimeSlot());
      dtos.add(dto);
    }

    return dtos;
  }

  /**
   * Finds a student by ID, student number, or email.
   *
   * @param identifier the identifier to search for
   * @return the found student
   */
  private Student findStudentFlexible(String identifier) {
    return studentRepository.findById(identifier)
        .orElseGet(() -> studentRepository.findByStudentId(identifier)
            .orElseGet(() -> studentRepository.findByEmail(identifier)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado: "
                    + identifier))));
  }
}

