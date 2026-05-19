package com.example.midaxus.model.dtos;

import java.time.LocalTime;

/**
 * Data Transfer Object for InstitutionPolicyDto.
 */
public class InstitutionPolicyDto {
  private Long id;
  private LocalTime classStartTime;
  private LocalTime classEndTime;
  private LocalTime lunchStartTime;
  private LocalTime lunchEndTime;
  private Integer standardCapacity;
  private Integer capacityTolerancePercent;
  private Integer maxSessionsPerWeek;

  
  /**
   * Constructor for Long getId.
   */
  
  /**
   * Gets the Id.
   */
  public Long getId() {
    return id;
  }

  
  /**
   * Constructor for void setId.
   */
  
  /**
   * Sets the Id.
   */
  public void setId(Long id) {
    this.id = id;
  }

  
  /**
   * Constructor for LocalTime getClassStartTime.
   */
  
  /**
   * Gets the ClassStartTime.
   */
  public LocalTime getClassStartTime() {
    return classStartTime;
  }

  
  /**
   * Constructor for void setClassStartTime.
   */
  
  /**
   * Sets the ClassStartTime.
   */
  public void setClassStartTime(LocalTime classStartTime) {
    this.classStartTime = classStartTime;
  }

  
  /**
   * Constructor for LocalTime getClassEndTime.
   */
  
  /**
   * Gets the ClassEndTime.
   */
  public LocalTime getClassEndTime() {
    return classEndTime;
  }

  
  /**
   * Constructor for void setClassEndTime.
   */
  
  /**
   * Sets the ClassEndTime.
   */
  public void setClassEndTime(LocalTime classEndTime) {
    this.classEndTime = classEndTime;
  }

  
  /**
   * Constructor for LocalTime getLunchStartTime.
   */
  
  /**
   * Gets the LunchStartTime.
   */
  public LocalTime getLunchStartTime() {
    return lunchStartTime;
  }

  
  /**
   * Constructor for void setLunchStartTime.
   */
  
  /**
   * Sets the LunchStartTime.
   */
  public void setLunchStartTime(LocalTime lunchStartTime) {
    this.lunchStartTime = lunchStartTime;
  }

  
  /**
   * Constructor for LocalTime getLunchEndTime.
   */
  
  /**
   * Gets the LunchEndTime.
   */
  public LocalTime getLunchEndTime() {
    return lunchEndTime;
  }

  
  /**
   * Constructor for void setLunchEndTime.
   */
  
  /**
   * Sets the LunchEndTime.
   */
  public void setLunchEndTime(LocalTime lunchEndTime) {
    this.lunchEndTime = lunchEndTime;
  }

  
  /**
   * Constructor for Integer getStandardCapacity.
   */
  
  /**
   * Gets the StandardCapacity.
   */
  public Integer getStandardCapacity() {
    return standardCapacity;
  }

  
  /**
   * Constructor for void setStandardCapacity.
   */
  
  /**
   * Sets the StandardCapacity.
   */
  public void setStandardCapacity(Integer standardCapacity) {
    this.standardCapacity = standardCapacity;
  }

  
  /**
   * Constructor for Integer getCapacityTolerancePercent.
   */
  
  /**
   * Gets the CapacityTolerancePercent.
   */
  public Integer getCapacityTolerancePercent() {
    return capacityTolerancePercent;
  }

  
  /**
   * Constructor for void setCapacityTolerancePercent.
   */
  
  /**
   * Sets the CapacityTolerancePercent.
   */
  public void setCapacityTolerancePercent(Integer capacityTolerancePercent) {
    this.capacityTolerancePercent = capacityTolerancePercent;
  }

  
  /**
   * Constructor for Integer getMaxSessionsPerWeek.
   */
  
  /**
   * Gets the MaxSessionsPerWeek.
   */
  public Integer getMaxSessionsPerWeek() {
    return maxSessionsPerWeek;
  }

  
  /**
   * Constructor for void setMaxSessionsPerWeek.
   */
  
  /**
   * Sets the MaxSessionsPerWeek.
   */
  public void setMaxSessionsPerWeek(Integer maxSessionsPerWeek) {
    this.maxSessionsPerWeek = maxSessionsPerWeek;
  }
}






















