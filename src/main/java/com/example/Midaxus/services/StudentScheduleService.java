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

      DayOfWeek day = parseDayOfWeek(dto.getDay());
      if (day == null) {
        continue;
      }

      StudentScheduleSlot entity = new StudentScheduleSlot(student, cg, day, dto.getSlot());
      entities.add(entity);
    }

    scheduleRepository.saveAll(entities);
  }

  private DayOfWeek parseDayOfWeek(String dayStr) {
    if (dayStr == null) return null;
    String cleanStr = dayStr.trim().toUpperCase()
        .replaceAll("[ÁÉÍÓÚ]", "AEIOU");
    switch (cleanStr) {
      case "LUNES": case "LUN":
        return DayOfWeek.MONDAY;
      case "MARTES": case "MAR":
        return DayOfWeek.TUESDAY;
      case "MIERCOLES": case "MIE":
        return DayOfWeek.WEDNESDAY;
      case "JUEVES": case "JUE":
        return DayOfWeek.THURSDAY;
      case "VIERNES": case "VIE":
        return DayOfWeek.FRIDAY;
      case "SABADO": case "SAB":
        return DayOfWeek.SATURDAY;
      case "DOMINGO": case "DOM":
        return DayOfWeek.SUNDAY;
      default:
        try {
          return DayOfWeek.valueOf(cleanStr);
        } catch (Exception e) {
          return null;
        }
    }
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
   * Retrieves all scheduled slots for a specific teacher.
   *
   * @param teacherId the ID or teacherCode of the teacher
   * @return a list of schedule slot DTOs
   */
  @Transactional(readOnly = true)
  public List<StudentScheduleSlotDto> getTeacherSchedule(String teacherId) {
    List<StudentScheduleSlot> entities = scheduleRepository.findByCourseGroup_Teacher_TeacherCode(teacherId);
    if (entities.isEmpty()) {
      entities = scheduleRepository.findByCourseGroup_Teacher_Id(teacherId);
    }
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

