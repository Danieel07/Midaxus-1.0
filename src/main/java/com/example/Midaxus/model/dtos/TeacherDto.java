package com.example.midaxus.model.dtos;

import java.util.Date;
import java.util.List;

/**
 * Data Transfer Object for TeacherDto.
 */
public class TeacherDto {

  private String id;
  private String teacherCode;
  private String userName;
  private Date startDate;
  private List<String> subjectsIds;
  private List<TeacherAvailabilityDto> availabilities;
  private String firstName;
  private String lastName;
  private String password;
  private String email;

  
  /**
   * Constructor for TeacherDto.
   */
  public TeacherDto() {}

  public TeacherDto(String id, String teacherCode, String userName,
      String firstName, String lastName, String email,
      String password) {
    this.id = id;
    this.teacherCode = teacherCode;
    this.userName = userName;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
  }

  
  /**
   * Constructor for String getId.
   */
  
  /**
   * Gets the Id.
   */
  public String getId() {
    return id;
  }

  
  /**
   * Constructor for void setId.
   */
  
  /**
   * Sets the Id.
   */
  public void setId(String id) {
    this.id = id;
  }

  
  /**
   * Constructor for String getTeacherCode.
   */
  
  /**
   * Gets the TeacherCode.
   */
  public String getTeacherCode() {
    return teacherCode;
  }

  
  /**
   * Constructor for void setTeacherCode.
   */
  
  /**
   * Sets the TeacherCode.
   */
  public void setTeacherCode(String teacherCode) {
    this.teacherCode = teacherCode;
  }

  
  /**
   * Constructor for String getUserName.
   */
  
  /**
   * Gets the UserName.
   */
  public String getUserName() {
    return userName;
  }

  
  /**
   * Constructor for void setUserName.
   */
  
  /**
   * Sets the UserName.
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  
  /**
   * Constructor for Date getStartDate.
   */
  
  /**
   * Gets the StartDate.
   */
  public Date getStartDate() {
    return startDate;
  }

  
  /**
   * Constructor for void setStartDate.
   */
  
  /**
   * Sets the StartDate.
   */
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  
  /**
   * Constructor for List<String> getSubjectsIds.
   */
  
  /**
   * Gets the SubjectsIds.
   */
  public List<String> getSubjectsIds() {
    return subjectsIds;
  }

  
  /**
   * Constructor for void setSubjectsIds.
   */
  
  /**
   * Sets the SubjectsIds.
   */
  public void setSubjectsIds(List<String> subjectsIds) {
    this.subjectsIds = subjectsIds;
  }

  
  /**
   * Constructor for List<TeacherAvailabilityDto> getAvailabilities.
   */
  
  /**
   * Gets the Availabilities.
   */
  public List<TeacherAvailabilityDto> getAvailabilities() {
    return availabilities;
  }

  
  /**
   * Constructor for void setAvailabilities.
   */
  
  /**
   * Sets the Availabilities.
   */
  public void setAvailabilities(List<TeacherAvailabilityDto> availabilities) {
    this.availabilities = availabilities;
  }

  
  /**
   * Constructor for String getFirstName.
   */
  
  /**
   * Gets the FirstName.
   */
  public String getFirstName() {
    return firstName;
  }

  
  /**
   * Constructor for void setFirstName.
   */
  
  /**
   * Sets the FirstName.
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  
  /**
   * Constructor for String getLastName.
   */
  
  /**
   * Gets the LastName.
   */
  public String getLastName() {
    return lastName;
  }

  
  /**
   * Constructor for void setLastName.
   */
  
  /**
   * Sets the LastName.
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  
  /**
   * Constructor for String getEmail.
   */
  
  /**
   * Gets the Email.
   */
  public String getEmail() {
    return email;
  }

  
  /**
   * Constructor for void setEmail.
   */
  
  /**
   * Sets the Email.
   */
  public void setEmail(String email) {
    this.email = email;
  }

  
  /**
   * Constructor for String getPassword.
   */
  
  /**
   * Gets the Password.
   */
  public String getPassword() {
    return password;
  }

  
  /**
   * Constructor for void setPassword.
   */
  
  /**
   * Sets the Password.
   */
  public void setPassword(String password) {
    this.password = password;
  }
}






















