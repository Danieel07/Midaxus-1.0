package com.example.midaxus.model.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for AcademicPeriodDto.
 */
public class AcademicPeriodDto {

  private String periodId;
  private String code;
  private String description;
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalDateTime enrollmentStartDate;
  private LocalDateTime enrollmentEndDate;

  
  /**
   * Constructor for AcademicPeriodDto.
   */
  public AcademicPeriodDto() {}

  public AcademicPeriodDto(String periodId, String code, String description,
     LocalDate startDate, LocalDate endDate,
     LocalDateTime enrollmentStartDate,
     LocalDateTime enrollmentEndDate) {
    this.periodId = periodId;
    this.code = code;
    this.description = description;
    this.startDate = startDate;
    this.endDate = endDate;
    this.enrollmentStartDate = enrollmentStartDate;
    this.enrollmentEndDate = enrollmentEndDate;
  }

  
  /**
   * Constructor for String getPeriodId.
   */
  
  /**
   * Gets the PeriodId.
   */
  public String getPeriodId() {
    return periodId;
  }

  
  /**
   * Constructor for void setPeriodId.
   */
  
  /**
   * Sets the PeriodId.
   */
  public void setPeriodId(String periodId) {
    this.periodId = periodId;
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
   * Constructor for String getDescription.
   */
  
  /**
   * Gets the Description.
   */
  public String getDescription() {
    return description;
  }

  
  /**
   * Constructor for void setDescription.
   */
  
  /**
   * Sets the Description.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  
  /**
   * Constructor for LocalDate getStartDate.
   */
  
  /**
   * Gets the StartDate.
   */
  public LocalDate getStartDate() {
    return startDate;
  }

  
  /**
   * Constructor for void setStartDate.
   */
  
  /**
   * Sets the StartDate.
   */
  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  
  /**
   * Constructor for LocalDate getEndDate.
   */
  
  /**
   * Gets the EndDate.
   */
  public LocalDate getEndDate() {
    return endDate;
  }

  
  /**
   * Constructor for void setEndDate.
   */
  
  /**
   * Sets the EndDate.
   */
  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  
  /**
   * Constructor for LocalDateTime getEnrollmentStartDate.
   */
  
  /**
   * Gets the EnrollmentStartDate.
   */
  public LocalDateTime getEnrollmentStartDate() {
    return enrollmentStartDate;
  }

  
  /**
   * Constructor for void setEnrollmentStartDate.
   */
  
  /**
   * Sets the EnrollmentStartDate.
   */
  public void setEnrollmentStartDate(LocalDateTime enrollmentStartDate) {
    this.enrollmentStartDate = enrollmentStartDate;
  }

  
  /**
   * Constructor for LocalDateTime getEnrollmentEndDate.
   */
  
  /**
   * Gets the EnrollmentEndDate.
   */
  public LocalDateTime getEnrollmentEndDate() {
    return enrollmentEndDate;
  }

  
  /**
   * Constructor for void setEnrollmentEndDate.
   */
  
  /**
   * Sets the EnrollmentEndDate.
   */
  public void setEnrollmentEndDate(LocalDateTime enrollmentEndDate) {
    this.enrollmentEndDate = enrollmentEndDate;
  }
}






















