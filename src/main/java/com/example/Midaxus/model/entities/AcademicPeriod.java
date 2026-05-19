package com.example.midaxus.model.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "academic_period")
/**
 * Entity class for AcademicPeriod.
 */
public class AcademicPeriod {

  @Id
  private String periodId;
  private String code;
  private String description;


  @Column(name = "start_date")
  private LocalDate startDate;
  @Column(name = "end_date")
  private LocalDate endDate;

  private LocalDateTime enrollmentStartDate;
  private LocalDateTime enrollmentEndDate;
  @OneToMany(mappedBy = "academicPeriod")
  private List<CourseGroup> courseGroups = new ArrayList<>();


  public AcademicPeriod(){}

  public AcademicPeriod(String periodId, String code, String description,
      LocalDate startDate, LocalDate endDate, LocalDateTime enrollmentStartDate,
      LocalDateTime enrollmentEndDate) {
    this.periodId = periodId;
    this.code = code;
    this.description = description;
    this.startDate = startDate;
    this.endDate = endDate;
    this.enrollmentStartDate = enrollmentStartDate;
    this.enrollmentEndDate = enrollmentEndDate;

  }

  public String getPeriodId() {
    return periodId;
  }

  public void setPeriodId(String periodId) {
    this.periodId = periodId;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public LocalDateTime getEnrollmentStartDate() {
    return enrollmentStartDate;
  }

  public void setEnrollmentStartDate(LocalDateTime enrollmentStartDate) {
    this.enrollmentStartDate = enrollmentStartDate;
  }

  public LocalDateTime getEnrollmentEndDate() {
    return enrollmentEndDate;
  }

  public void setEnrollmentEndDate(LocalDateTime enrollmentEndDate) {
    this.enrollmentEndDate = enrollmentEndDate;
  }

  public List<CourseGroup> getCourseGroups() {
    return courseGroups;
  }

  public void setCourseGroups(List<CourseGroup> courseGroups) {
    this.courseGroups = courseGroups;
  }
}






















