package com.example.midaxus.model.entities;

import jakarta.persistence.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subject")
/**
 * Entity class for Subject.
 */
public class Subject {

  @Id
  private String idSubject;

  @Column(name = "subject_name")
  private String subjectName;

  @Column(name = "sessions_per_week")
  private int sessionPerWeek;

  @Column(name = "duration_minutes")
  private int durationMinutes;

  @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CourseGroup> courseGroups = new ArrayList<>();

  @ManyToMany(mappedBy = "habilitatedSubjects")
  private List<Teacher> teachers = new ArrayList<>();

  public Subject() {}

  public Subject(String idSubject, String subjectName, int sessionPerWeek, int durationMinutes) {
    this.idSubject = idSubject;
    this.subjectName = subjectName;
    this.sessionPerWeek = sessionPerWeek;
    this.durationMinutes = durationMinutes;
  }

  public String getIdSubject() {
    return idSubject;
  }

  public void setIdSubject(String idSubject) {
    this.idSubject = idSubject;
  }

  public String getSubjectName() {
    return subjectName;
  }

  public void setSubjectName(String subjectName) {
    this.subjectName = subjectName;
  }

  public int getSessionPerWeek() {
    return sessionPerWeek;
  }

  public void setSessionPerWeek(int sessionPerWeek) {
    this.sessionPerWeek = sessionPerWeek;
  }


  public int getDurationMinutes() {
    return durationMinutes;
  }

  public void setDurationMinutes(int durationMinutes) {
    this.durationMinutes = durationMinutes;
  }

  public List<CourseGroup> getCourseGroups() {
    return courseGroups;
  }

  public void setCourseGroups(List<CourseGroup> courseGroups) {
    this.courseGroups = courseGroups;
  }

  public List<Teacher> getTeachers() {
    return teachers;
  }

  public void setTeachers(List<Teacher> teachers) {
    this.teachers = teachers;
  }
}






















