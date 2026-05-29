package com.example.midaxus.model.dtos.auth;

/**
 * Data Transfer Object for LoginResponseDto.
 */
public class LoginResponseDto {
  private boolean success;
  private String message;
  private String userType;
  private String token;
  private Object data;

  
  /**
   * Constructor for LoginResponseDto.
   */
  public LoginResponseDto(boolean success, String message, String userType, String token, Object data) {
    this.success = success;
    this.message = message;
    this.userType = userType;
    this.token = token;
    this.data = data;
  }

  // Getters y Setters

  
  /**
   * Constructor for boolean isSuccess.
   */
  public boolean isSuccess() {
    return success;
  }

  
  /**
   * Constructor for void setSuccess.
   */
  
  /**
   * Sets the Success.
   */
  public void setSuccess(boolean success) {
    this.success = success;
  }

  
  /**
   * Constructor for String getMessage.
   */
  
  /**
   * Gets the Message.
   */
  public String getMessage() {
    return message;
  }

  
  /**
   * Constructor for void setMessage.
   */
  
  /**
   * Sets the Message.
   */
  public void setMessage(String message) {
    this.message = message;
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
   * Constructor for String getToken.
   */
  
  /**
   * Gets the Token.
   */
  public String getToken() {
    return token;
  }

  
  /**
   * Constructor for void setToken.
   */
  
  /**
   * Sets the Token.
   */
  public void setToken(String token) {
    this.token = token;
  }

  
  /**
   * Constructor for Object getData.
   */
  
  /**
   * Gets the Data.
   */
  public Object getData() {
    return data;
  }

  
  /**
   * Constructor for void setData.
   */
  
  /**
   * Sets the Data.
   */
  public void setData(Object data) {
    this.data = data;
  }
}






















