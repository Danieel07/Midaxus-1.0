package com.example.midaxus.model.dtos;

/**
 * Data Transfer Object for CourseGroupDto.
 */
public class CourseGroupDto {

  private String courseGroupId;
  private String teacherId;
  private String subjectId;
  private String academicPeriodId;
  private String code;
  private int capacity;

  
  /**
   * Constructor for CourseGroupDto.
   */
  public CourseGroupDto() {}

  public CourseGroupDto(String courseGroupId, String teacherId, String subjectId,
      String academicPeriodId, String code, int capacity) {
    this.courseGroupId = courseGroupId;
    this.teacherId = teacherId;
    this.subjectId = subjectId;
    this.academicPeriodId = academicPeriodId;
    this.code = code;
    this.capacity = capacity;
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
   * Constructor for String getTeacherId.
   */
  
  /**
   * Gets the TeacherId.
   */
  public String getTeacherId() {
    return teacherId;
  }

  
  /**
   * Constructor for void setTeacherId.
   */
  
  /**
   * Sets the TeacherId.
   */
  public void setTeacherId(String teacherId) {
    this.teacherId = teacherId;
  }

  
  /**
   * Constructor for String getSubjectId.
   */
  
  /**
   * Gets the SubjectId.
   */
  public String getSubjectId() {
    return subjectId;
  }

  
  /**
   * Constructor for void setSubjectId.
   */
  
  /**
   * Sets the SubjectId.
   */
  public void setSubjectId(String subjectId) {
    this.subjectId = subjectId;
  }

  
  /**
   * Constructor for String getAcademicPeriodId.
   */
  
  /**
   * Gets the AcademicPeriodId.
   */
  public String getAcademicPeriodId() {
    return academicPeriodId;
  }

  
  /**
   * Constructor for void setAcademicPeriodId.
   */
  
  /**
   * Sets the AcademicPeriodId.
   */
  public void setAcademicPeriodId(String academicPeriodId) {
    this.academicPeriodId = academicPeriodId;
  }

  
  /**
   * Constructor for String getCode.
   */
  
  /**
   * Gets the Code.
   */
  public String getCode() {
    return code;
  }

  
  /**
   * Constructor for void setCode.
   */
  
  /**
   * Sets the Code.
   */
  public void setCode(String code) {
    this.code = code;
  }

  
  /**
   * Constructor for int getCapacity.
   */
  
  /**
   * Gets the Capacity.
   */
  public int getCapacity() {
    return capacity;
  }

  
  /**
   * Constructor for void setCapacity.
   */
  
  /**
   * Sets the Capacity.
   */
  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }
}






















