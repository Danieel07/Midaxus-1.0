package com.example.midaxus.model.dtos;

import com.example.midaxus.model.entities.CourseGroup;
import com.example.midaxus.model.entities.Student;
import com.example.midaxus.model.enums.EnrollmentStatus;

/**
 * Data Transfer Object for EnrollmentDto.
 */
public class EnrollmentDto {

  private String enrollmentId;
  private String studentId;
  private String courseGroupId;
  private EnrollmentStatus status;

  
  /**
   * Constructor for EnrollmentDto.
   */
  public EnrollmentDto() {}

  public EnrollmentDto(String enrollmentId, String studentId,
     String courseGroupId, EnrollmentStatus status) {
    this.enrollmentId = enrollmentId;
    this.studentId = studentId;
    this.courseGroupId = courseGroupId;
    this.status = status;


  // getters & setters
}

  
  /**
   * Constructor for String getEnrollmentId.
   */
  
  /**
   * Gets the EnrollmentId.
   */
  public String getEnrollmentId() {
    return enrollmentId;
  }

  
  /**
   * Constructor for void setEnrollmentId.
   */
  
  /**
   * Sets the EnrollmentId.
   */
  public void setEnrollmentId(String enrollmentId) {
    this.enrollmentId = enrollmentId;
  }

  
  /**
   * Constructor for String getStudentId.
   */
  
  /**
   * Gets the StudentId.
   */
  public String getStudentId() {
    return studentId;
  }

  
  /**
   * Constructor for void setStudentId.
   */
  
  /**
   * Sets the StudentId.
   */
  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }

  
  /**
   * Constructor for String getCourseGroupId.
   */
  
  /**
   * Gets the CourseGroupId.
   */
  public String getCourseGroupId() {
    return courseGroupId;
  }

  
  /**
   * Constructor for void setCourseGroupId.
   */
  
  /**
   * Sets the CourseGroupId.
   */
  public void setCourseGroupId(String courseGroupId) {
    this.courseGroupId = courseGroupId;
  }

  
  /**
   * Constructor for EnrollmentStatus getStatus.
   */
  
  /**
   * Gets the Status.
   */
  public EnrollmentStatus getStatus() {
    return status;
  }

  
  /**
   * Constructor for void setStatus.
   */
  
  /**
   * Sets the Status.
   */
  public void setStatus(EnrollmentStatus status) {
    this.status = status;
  }
}






















