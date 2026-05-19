package com.example.midaxus.model.entities;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "teacher_availability")
/**
 * Entity class for TeacherAvailability.
 */
public class TeacherAvailability {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "teacher_id")
  private Teacher teacher;

  @Column(name = "day_of_week")
  private String dayOfWeek;

  @Column(name = "start_time")
  private LocalTime startTime;

  @Column(name = "end_time")
  private LocalTime endTime;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Teacher getTeacher() { return teacher; }
  public void setTeacher(Teacher teacher) { this.teacher = teacher; }

  public String getDayOfWeek() { return dayOfWeek; }
  public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

  public LocalTime getStartTime() { return startTime; }
  public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

  public LocalTime getEndTime() { return endTime; }
  public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
}






















