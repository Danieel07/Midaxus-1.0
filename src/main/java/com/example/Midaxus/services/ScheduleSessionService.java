package com.example.midaxus.services;

import com.example.midaxus.model.dtos.ScheduleSessionDto;
import com.example.midaxus.model.entities.CourseGroup;
import com.example.midaxus.model.entities.ScheduleSession;
import com.example.midaxus.model.entities.VinculationSlot;
import com.example.midaxus.repositories.CourseGroupRepository;
import com.example.midaxus.repositories.ScheduleSessionRepository;
import com.example.midaxus.repositories.VinculationSlotRepository;
import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing schedule sessions.
 */
@Service
public class ScheduleSessionService implements IScheduleSessionService {

  private final ScheduleSessionRepository scheduleSessionRepository;
  private final CourseGroupRepository courseGroupRepository;
  private final VinculationSlotRepository vinculationSlotRepository;

  /**
   * Constructs a ScheduleSessionService with the required repositories.
   *
   * @param scheduleSessionRepository the repository for schedule sessions
   * @param courseGroupRepository the repository for course groups
   * @param vinculationSlotRepository the repository for vinculation slots
   */
  public ScheduleSessionService(
      ScheduleSessionRepository scheduleSessionRepository,
      CourseGroupRepository courseGroupRepository,
      VinculationSlotRepository vinculationSlotRepository) {
    this.scheduleSessionRepository = scheduleSessionRepository;
    this.courseGroupRepository = courseGroupRepository;
    this.vinculationSlotRepository = vinculationSlotRepository;
  }

  /**
   * Saves a schedule session based on the provided DTO.
   *
   * @param dto the DTO containing session data
   * @return the saved ScheduleSession entity
   */
  @Override
  public ScheduleSession saveSession(ScheduleSessionDto dto) {
    ScheduleSession session = new ScheduleSession();
    session.setScheduleSessionId(UUID.randomUUID().toString());

    if (dto.getCourseGroupId() != null) {
      courseGroupRepository.findById(dto.getCourseGroupId()).ifPresentOrElse(
          session::setCourseGroup,
          () -> {
            CourseGroup cg = new CourseGroup();
            cg.setCourseGroupId(dto.getCourseGroupId());
            cg.setCode(dto.getCourseCode() != null ? dto.getCourseCode() : dto.getCourseGroupId());
            cg = courseGroupRepository.save(cg);
            session.setCourseGroup(cg);
          }
      );
    } else if (dto.getCourseCode() != null) {
      CourseGroup cg = new CourseGroup();
      cg.setCourseGroupId(UUID.randomUUID().toString());
      cg.setCode(dto.getCourseCode());
      cg = courseGroupRepository.save(cg);
      session.setCourseGroup(cg);
    }

    if (dto.getVinculationSlotId() != null) {
      vinculationSlotRepository.findById(dto.getVinculationSlotId())
          .ifPresent(session::setVinculationSlot);
    } else if (dto.getDay() != null) {
      // Find or create VinculationSlot
      VinculationSlot vs = new VinculationSlot();
      vs.setVinculationSlotId(UUID.randomUUID().toString());
      try {
        vs.setDay(DayOfWeek.valueOf(dto.getDay().toUpperCase()));
      } catch (Exception e) {
        vs.setDay(DayOfWeek.MONDAY); // fallback
      }
      vs = vinculationSlotRepository.save(vs);
      session.setVinculationSlot(vs);
    }

    return scheduleSessionRepository.save(session);
  }

  /**
   * Retrieves all schedule sessions associated with a specific course group.
   *
   * @param courseGroupId the ID of the course group
   * @return a list of schedule sessions
   */
  @Override
  public List<ScheduleSession> getSessionsByCourse(String courseGroupId) {
    return scheduleSessionRepository.findAll().stream()
        .filter(s -> s.getCourseGroup() != null
            && s.getCourseGroup().getCourseGroupId().equals(courseGroupId))
        .toList();
  }
}

