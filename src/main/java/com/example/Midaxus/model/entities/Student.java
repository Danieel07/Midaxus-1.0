package com.example.midaxus.model.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "student")
@DiscriminatorValue("STUDENT")
/**
 * Entity class for Student extends User.
 */
public class Student extends User {

  @Column(unique = true)
  private String studentId;

  @OneToMany(mappedBy = "student")
  List<Enrollment>enrollments = new ArrayList<>();

  public Student() {
  }

  public Student(String studentId, String userName, String firstName,
     String lastName, String id, String email,
     String password, Date signInDate) {

    super(userName, firstName, lastName, id, email, password, signInDate);
    this.studentId = studentId;
  }

  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }
}






















