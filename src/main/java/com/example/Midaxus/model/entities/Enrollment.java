package com.example.midaxus.model.entities;

import com.example.midaxus.model.enums.EnrollmentStatus;
import jakarta.persistence.*;

import javax.print.attribute.standard.MediaSize;

@Entity
@Table(name = "enrollment")
/**
 * Entity class for Enrollment.
 */
public class Enrollment {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String enrollmentId;

  @ManyToOne
  @JoinColumn(name = "student_id")
  private Student student;
  @ManyToOne
  @JoinColumn(name = "course_group_id")
  private CourseGroup courseGroup;

  @Enumerated(EnumType.STRING)
  private EnrollmentStatus status;

  public Enrollment(String enrollmentId, Student student,
      CourseGroup courseGroup, EnrollmentStatus status) {
    this.enrollmentId = enrollmentId;
    this.student = student;
    this.courseGroup = courseGroup;
    this.status = status;
  }
  public Enrollment(){}


  public String getEnrollmentId() {
    return enrollmentId;
  }

  public void setEnrollmentId(String enrollmentId) {
    this.enrollmentId = enrollmentId;
  }

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public CourseGroup getCourseGroup() {
    return courseGroup;
  }

  public void setCourseGroup(CourseGroup courseGroup) {
    this.courseGroup = courseGroup;
  }

  public EnrollmentStatus getStatus() {
    return status;
  }

  public void setStatus(EnrollmentStatus status) {
    this.status = status;
  }
}






















