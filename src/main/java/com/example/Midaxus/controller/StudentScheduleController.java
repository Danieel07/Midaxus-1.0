package com.example.midaxus.controller;

import com.example.midaxus.model.dtos.ApiResponse;
import com.example.midaxus.model.dtos.StudentScheduleSlotDto;
import com.example.midaxus.services.StudentScheduleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for managing student schedules.
 */
@RestController
@RequestMapping("/api/student-schedules")
public class StudentScheduleController {

  @Autowired
  private StudentScheduleService studentScheduleService;

  /**
   * Saves the schedule for a specific student.
   *
   * @param studentId the ID of the student
   * @param slots the list of schedule slots to save
   * @return a ResponseEntity containing an ApiResponse indicating success
   */
  @PostMapping("/{studentId:.+}")
  public ResponseEntity<ApiResponse<Void>> saveSchedule(
      @PathVariable String studentId,
      @RequestBody List<StudentScheduleSlotDto> slots) {
    
    studentScheduleService.saveStudentSchedule(studentId, slots);
    return ResponseEntity.ok(new ApiResponse<>(null, "Horario guardado exitosamente", null));
  }

  /**
   * Retrieves the schedule for a specific student.
   *
   * @param studentId the ID of the student
   * @return a ResponseEntity containing a list of student schedule slot DTOs
   */
  @GetMapping("/{studentId:.+}")
  public ResponseEntity<List<StudentScheduleSlotDto>> getSchedule(@PathVariable String studentId) {
    return ResponseEntity.ok(studentScheduleService.getStudentSchedule(studentId));
  }

  /**
   * Retrieves the schedule for a specific teacher.
   *
   * @param teacherId the ID or teacherCode of the teacher
   * @return a ResponseEntity containing a list of schedule slot DTOs
   */
  @GetMapping("/teacher/{teacherId:.+}")
  public ResponseEntity<List<StudentScheduleSlotDto>> getTeacherSchedule(@PathVariable String teacherId) {
    return ResponseEntity.ok(studentScheduleService.getTeacherSchedule(teacherId));
  }
}

