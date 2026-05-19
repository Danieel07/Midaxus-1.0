package com.example.midaxus.services;

import com.example.midaxus.model.entities.ScheduleSession;
import java.util.List;

/**
 * Interface for Schedule Validation service operations.
 */
public interface IScheduleValidation {

  /**
   * Validates that there are no consecutive sessions for the same subject in the same day.
   *
   * @param sessions the list of schedule sessions to validate
   * @return a list of validation error messages
   */
  List<String> validateNoConsecutiveSessions(List<ScheduleSession> sessions);
}

