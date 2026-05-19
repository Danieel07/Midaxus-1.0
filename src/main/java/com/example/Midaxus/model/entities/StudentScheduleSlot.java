package com.example.midaxus.model.entities;

import jakarta.persistence.*;
import java.time.DayOfWeek;

@Entity
@Table(name = "student_schedule_slot")
/**
 * Entity class for StudentScheduleSlot.
 */
public class StudentScheduleSlot {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @ManyToOne
  @JoinColumn(name = "student_id", nullable = false)
  private Student student;

  @ManyToOne
  @JoinColumn(name = "course_group_id", nullable = false)
  private CourseGroup courseGroup;

  @Column(name = "day_of_week", nullable = false)
  @Enumerated(EnumType.STRING)
  private DayOfWeek day;

  @Column(name = "time_slot", nullable = false)
  private String timeSlot; // e.g., "08:00-09:30"

  public StudentScheduleSlot() {}

  public StudentScheduleSlot(Student student, CourseGroup courseGroup, DayOfWeek day, String timeSlot) {
    this.student = student;
    this.courseGroup = courseGroup;
    this.day = day;
    this.timeSlot = timeSlot;
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public Student getStudent() { return student; }
  public void setStudent(Student student) { this.student = student; }

  public CourseGroup getCourseGroup() { return courseGroup; }
  public void setCourseGroup(CourseGroup courseGroup) { this.courseGroup = courseGroup; }

  public DayOfWeek getDay() { return day; }
  public void setDay(DayOfWeek day) { this.day = day; }

  public String getTimeSlot() { return timeSlot; }
  public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
}






















