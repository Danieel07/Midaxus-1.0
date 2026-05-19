package com.example.midaxus.services;

import com.example.midaxus.model.entities.CourseGroup;
import com.example.midaxus.model.entities.ScheduleSession;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Service implementation for validating academic schedules.
 */
@Service
public class ScheduleValidationService implements IScheduleValidation {

  /**
   * Validates that there are no consecutive daily sessions for the same course group.
   *
   * @param sessions the list of schedule sessions to validate
   * @return a list of warning messages for any violations found
   */
  @Override
  public List<String> validateNoConsecutiveSessions(List<ScheduleSession> sessions) {
    List<String> warnings = new ArrayList<>();

    Map<CourseGroup, List<ScheduleSession>> sessionsByCourse = sessions.stream()
        .collect(Collectors.groupingBy(ScheduleSession::getCourseGroup));

    for (Map.Entry<CourseGroup, List<ScheduleSession>> entry : sessionsByCourse.entrySet()) {
      CourseGroup courseGroup = entry.getKey();
      List<ScheduleSession> courseSessions = entry.getValue();

      List<DayOfWeek> days = courseSessions.stream()
          .map(s -> s.getVinculationSlot().getDay())
          .sorted(Comparator.comparingInt(DayOfWeek::getValue))
          .toList();

      for (int i = 0; i < days.size() - 1; i++) {
        DayOfWeek current = days.get(i);
        DayOfWeek next = days.get(i + 1);

        if (next.getValue() - current.getValue() == 1) {
          warnings.add("Curso " + courseGroup
              + " tiene sesiones consecutivas: " + current + " y " + next);
        }
      }
    }

    return warnings;
  }
}

