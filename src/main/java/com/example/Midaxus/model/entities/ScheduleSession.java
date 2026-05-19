package com.example.midaxus.model.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "schedule_session")
/**
 * Entity class for ScheduleSession.
 */
public class ScheduleSession {

  @Id
  private String scheduleSessionId;

  @ManyToOne
  @JoinColumn(name = "schedule_id")
  private Schedule schedule;

  @ManyToOne
  @JoinColumn(name = "course_group_id")
  private CourseGroup courseGroup;

  @ManyToOne
  @JoinColumn(name = "room_id")
  private Room room;

  @ManyToOne
  @JoinColumn(name = "vinculation_slot_id")
  private VinculationSlot vinculationSlot;

  public ScheduleSession() {
  }

  public ScheduleSession(String scheduleSessionId, Schedule schedule, CourseGroup courseGroup, Room room,
       VinculationSlot vinculationSlot ) {
  }

  public String getScheduleSessionId() {

    return scheduleSessionId;
  }

  public void setScheduleSessionId(String scheduleSessionId) {
    this.scheduleSessionId = scheduleSessionId;
  }

  public Schedule getSchedule() {
    return schedule;
  }

  public void setSchedule(Schedule schedule) {
    this.schedule = schedule;
  }

  public CourseGroup getCourseGroup() {
    return courseGroup;
  }

  public void setCourseGroup(CourseGroup courseGroup) {
    this.courseGroup = courseGroup;
  }

  public Room getRoom() {
    return room;
  }

  public void setRoom(Room room) {
    this.room = room;
  }

  public VinculationSlot getVinculationSlot() {
    return vinculationSlot;
  }

  public void setVinculationSlot(VinculationSlot vinculationSlot) {
    this.vinculationSlot = vinculationSlot;
  }
}






















