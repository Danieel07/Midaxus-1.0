package com.example.midaxus.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "schedule")
/**
 * Entity class for Schedule.
 */
public class Schedule {
  @Id
  private String id;
  
  public Schedule() {}
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
}






















