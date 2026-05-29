package com.example.midaxus.model.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "teacher")
@DiscriminatorValue("TEACHER")
/**
 * Entity class for Teacher extends User.
 */
public class Teacher extends User {

  @Column(unique = true)
  private String teacherCode;

  @Temporal(TemporalType.DATE)
  private Date startDate;

  @OneToMany(mappedBy = "teacher")
  private List<CourseGroup> courseGroups;

  @ManyToMany
  @JoinTable(
    name = "teacher_subjects",
    joinColumns = @JoinColumn(name = "teacher_id"),
    inverseJoinColumns = @JoinColumn(name = "subject_id")
  )
  private List<Subject> habilitatedSubjects = new ArrayList<>();

  @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TeacherAvailability> availabilities = new ArrayList<>();



  public Teacher() {}

  public Teacher(String teacherCode, String userName, String firstName,
     String lastName, String id, String email,
     String password, Date signInDate,
     Date startDate) {

    super(userName, firstName, lastName, id, email, password, signInDate);
    this.teacherCode = teacherCode;
    this.startDate = startDate;
  }

  public String getTeacherCode() {
    return teacherCode;
  }

  public void setTeacherCode(String teacherCode) {
    this.teacherCode = teacherCode;
  }

  public List<CourseGroup> getCourseGroups() {
    return courseGroups;
  }

  public void setCourseGroups(List<CourseGroup> courseGroups) {
    this.courseGroups = courseGroups;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public List<Subject> getHabilitatedSubjects() {
    return habilitatedSubjects;
  }

  public void setHabilitatedSubjects(List<Subject> habilitatedSubjects) {
    this.habilitatedSubjects = habilitatedSubjects;
  }

  public List<TeacherAvailability> getAvailabilities() {
    return availabilities;
  }

  public void setAvailabilities(List<TeacherAvailability> availabilities) {
    this.availabilities = availabilities;
  }
}






















