package com.example.midaxus.model.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "admin")
@DiscriminatorValue("ADMIN")
/**
 * Entity class for Admin extends User.
 */
public class Admin extends User {

  @Column(unique = true)
  private String adminId;

  public Admin() {}

  public Admin(String adminId, String userName, String firstName,
       String lastName, String id, String email,
       String password, Date signInDate) {

    super(userName, firstName, lastName, id, email, password, signInDate);
    this.adminId = adminId;
  }

  public String getAdminId() {
    return adminId;
  }

  public void setAdminId(String adminId) {
    this.adminId = adminId;
  }
}






















