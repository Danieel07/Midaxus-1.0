package com.example.midaxus.model.dtos;

import java.time.LocalTime;

/**
 * Data Transfer Object for TeacherAvailabilityDto.
 */
public class TeacherAvailabilityDto {
  private String dayOfWeek;
  private LocalTime startTime;
  private LocalTime endTime;

  
  /**
   * Constructor for TeacherAvailabilityDto.
   */
  public TeacherAvailabilityDto() {}

  
  /**
   * Constructor for TeacherAvailabilityDto.
   */
  public TeacherAvailabilityDto(String dayOfWeek, LocalTime startTime, LocalTime endTime) {
    this.dayOfWeek = dayOfWeek;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  
  /**
   * Constructor for String getDayOfWeek.
   */
  
  /**
   * Gets the DayOfWeek.
   */
  public String getDayOfWeek() { return dayOfWeek; }
  
  /**
   * Constructor for void setDayOfWeek.
   */
  
  /**
   * Sets the DayOfWeek.
   */
  public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

  
  /**
   * Constructor for LocalTime getStartTime.
   */
  
  /**
   * Gets the StartTime.
   */
  public LocalTime getStartTime() { return startTime; }
  
  /**
   * Constructor for void setStartTime.
   */
  
  /**
   * Sets the StartTime.
   */
  public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

  
  /**
   * Constructor for LocalTime getEndTime.
   */
  
  /**
   * Gets the EndTime.
   */
  public LocalTime getEndTime() { return endTime; }
  
  /**
   * Constructor for void setEndTime.
   */
  
  /**
   * Sets the EndTime.
   */
  public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
}






















