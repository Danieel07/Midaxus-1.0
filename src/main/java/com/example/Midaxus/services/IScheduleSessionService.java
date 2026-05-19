package com.example.midaxus.services;

import com.example.midaxus.model.dtos.ScheduleSessionDto;
import com.example.midaxus.model.entities.ScheduleSession;
import java.util.List;

/**
 * Interface for Schedule Session service operations.
 */
public interface IScheduleSessionService {

  /**
   * Saves a schedule session.
   *
   * @param dto the schedule session DTO to save
   * @return the saved schedule session entity
   */
  ScheduleSession saveSession(ScheduleSessionDto dto);

  /**
   * Retrieves schedule sessions for a specific course group.
   *
   * @param courseGroupId the ID of the course group
   * @return a list of schedule session entities
   */
  List<ScheduleSession> getSessionsByCourse(String courseGroupId);
}

