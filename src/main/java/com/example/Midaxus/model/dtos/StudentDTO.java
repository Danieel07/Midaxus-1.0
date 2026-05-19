package com.example.midaxus.model.dtos;

import java.util.Date;

/**
 * Data Transfer Object for StudentDto.
 */
public class StudentDto {


  private String id;
  private String studentId;
  private String firstName;
  private String lastName;
  private String userName;
  private String email;
  private String password;
  private Date signInDate;

  
  /**
   * Constructor for StudentDto.
   */
  public StudentDto() {}

  public StudentDto( String id, String studentId, String firstName, String lastName,
       String userName, String email, String password, Date signInDate ) {

    this.id = id;
    this.studentId = studentId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.userName = userName;
    this.email = email;
    this.password = password;
    this.signInDate = signInDate;
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
   * Constructor for String getStudentId.
   */
  
  /**
   * Gets the StudentId.
   */
  public String getStudentId() {
    return studentId;
  }

  
  /**
   * Constructor for void setStudentId.
   */
  
  /**
   * Sets the StudentId.
   */
  public void setStudentId(String studentId) {
    this.studentId = studentId;
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

  
  /**
   * Constructor for Date getSignInDate.
   */
  
  /**
   * Gets the SignInDate.
   */
  public Date getSignInDate() {
    return signInDate;
  }

  
  /**
   * Constructor for void setSignInDate.
   */
  
  /**
   * Sets the SignInDate.
   */
  public void setSignInDate(Date signInDate) {
    this.signInDate = signInDate;
  }


}






















