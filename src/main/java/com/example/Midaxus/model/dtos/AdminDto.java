package com.example.midaxus.model.dtos;

/**
 * Data Transfer Object for AdminDto.
 */
public class AdminDto {

  private String adminId;
  private String userName;
  private String email;
  private String firstName;
  private String lastName;
  private String password;

  
  /**
   * Constructor for AdminDto.
   */
  public AdminDto() {}

  
  /**
   * Constructor for AdminDto.
   */
  public AdminDto(String adminId, String firstName, String lastName,  String userName, String email) {

    this.adminId = adminId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.userName = userName;
    this.email = email;
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
   * Constructor for void setEmail.
   */
  
  /**
   * Sets the Email.
   */
  public void setEmail(String email) {
    this.email = email;
  }


  @Override
  
  /**
   * Constructor for String toString.
   */
  public String toString() {
    return "AdminDto{" +
      "adminId='" + adminId + '\'' +
      ", userName='" + userName + '\'' +
      ", email='" + email + '\'' +
      '}';
  }
}






















