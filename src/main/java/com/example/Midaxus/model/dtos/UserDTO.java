package com.example.midaxus.model.dtos;

/**
 * Data Transfer Object for UserDto.
 */
public class UserDto {

  private String userType;
  private String teacherCode;
  private String studentId;
  private String adminId;
  private String userName;
  private String firstName;
  private String lastName;
  private String email;
  private String password;

  public UserDto(){}

  public UserDto(String userType,String teacherCode,String studentId, String adminId, String userName, String firstName,
     String lastName, String email, String password) {
    this.userType = userType;
    this.teacherCode = teacherCode;
    this.studentId = studentId;
    this.adminId = adminId;
    this.userName = userName;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
  }

  
  /**
   * Constructor for String getAdminId.
   */
  
  /**
   * Gets the AdminId.
   */
  public String getAdminId() {
    return adminId;
  }

  
  /**
   * Constructor for void setAdminId.
   */
  
  /**
   * Sets the AdminId.
   */
  public void setAdminId(String adminId) {
    this.adminId = adminId;
  }

  
  /**
   * Constructor for String getUserType.
   */
  
  /**
   * Gets the UserType.
   */
  public String getUserType() {
    return userType;
  }

  
  /**
   * Constructor for void setUserType.
   */
  
  /**
   * Sets the UserType.
   */
  public void setUserType(String userType) {
    this.userType = userType;
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






















