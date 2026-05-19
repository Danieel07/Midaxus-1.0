package com.example.midaxus.model.entities;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "vinculation_slot")
/**
 * Entity class for VinculationSlot.
 */
public class VinculationSlot {

  @Id
  private String vinculationSlotId;

  @Enumerated(EnumType.STRING)
  @Column(name = "day")
  private DayOfWeek day;

  @Column(name = "start_time")
  private LocalTime startTime;

  @Column(name = "end_time")
  private LocalTime endTime;

  public VinculationSlot() {}

  public VinculationSlot(String vinculationSlotId, DayOfWeek day,
       LocalTime startTime, LocalTime endTime) {
    this.vinculationSlotId = vinculationSlotId;
    this.day = day;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public String getVinculationSlotId() {
    return vinculationSlotId;
  }

  public void setVinculationSlotId(String vinculationSlotId) {
    this.vinculationSlotId = vinculationSlotId;
  }

  public DayOfWeek getDay() {
    return day;
  }

  public void setDay(DayOfWeek day) {
    this.day = day;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalTime startTime) {
    this.startTime = startTime;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalTime endTime) {
    this.endTime = endTime;
  }
}






















