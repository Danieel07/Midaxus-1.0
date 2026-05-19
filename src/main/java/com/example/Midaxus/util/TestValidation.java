package com.example.midaxus.util;

import com.example.midaxus.model.entities.*;
import com.example.midaxus.services.ScheduleValidationService;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TestValidation {

  public static void main(String[] args) {

    ScheduleValidationService service = new ScheduleValidationService();

    // Mock de CourseGroup
    CourseGroup course = new CourseGroup();
    course.setCode("Curso-1");

    // Slots
    VinculationSlot monday = new VinculationSlot(
      "1", DayOfWeek.MONDAY,
      LocalTime.of(8, 0),
      LocalTime.of(10, 0)
    );

    VinculationSlot tuesday = new VinculationSlot(
      "2", DayOfWeek.TUESDAY,
      LocalTime.of(8, 0),
      LocalTime.of(10, 0)
    );

    // Sesiones
    ScheduleSession s1 = new ScheduleSession();
    s1.setCourseGroup(course);
    s1.setVinculationSlot(monday);

    ScheduleSession s2 = new ScheduleSession();
    s2.setCourseGroup(course);
    s2.setVinculationSlot(tuesday);

    List<ScheduleSession> sessions = new ArrayList<>();
    sessions.add(s1);
    sessions.add(s2);

    // Validar
    List<String> warnings = service.validateNoConsecutiveSessions(sessions);

    warnings.forEach(System.out::println);
  }
}





















