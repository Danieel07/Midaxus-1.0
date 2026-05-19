package com.example.midaxus.controller;

import com.example.midaxus.model.dtos.ApiResponse;
import com.example.midaxus.model.dtos.ScheduleSessionDto;
import com.example.midaxus.model.entities.ScheduleSession;
import com.example.midaxus.services.IScheduleSessionService;
import com.example.midaxus.services.IScheduleValidation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for managing schedule sessions.
 */
@RestController
@RequestMapping("/api/schedule-sessions")
public class ScheduleSessionController {

  @Autowired
  private IScheduleSessionService scheduleSessionService;

  @Autowired
  private IScheduleValidation scheduleValidationService;

  /**
   * Creates a new schedule session and validates it.
   *
   * @param dto the schedule session DTO to create
   * @return a ResponseEntity containing the created session and any validation warnings
   */
  @PostMapping
  public ResponseEntity<ApiResponse<ScheduleSessionDto>> createSession(
      @RequestBody ScheduleSessionDto dto) {
    
    // 1. Save session normally (persisted in DB)
    ScheduleSession savedSession = scheduleSessionService.saveSession(dto);
    
    // 2. Get all sessions for this course to validate the business rule
    List<ScheduleSession> courseSessions = 
        scheduleSessionService.getSessionsByCourse(dto.getCourseGroupId());
    
    // 3. Invoke ScheduleValidationService
    List<String> warnings = scheduleValidationService.validateNoConsecutiveSessions(courseSessions);
    
    // 4. Return dto with success message and any soft warnings
    // For simplicity, we just return the same dto we received, but updated with the ID
    dto.setScheduleSessionId(savedSession.getScheduleSessionId());
    
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(new ApiResponse<>(dto, "Sesión creada exitosamente", warnings));
  }
}

