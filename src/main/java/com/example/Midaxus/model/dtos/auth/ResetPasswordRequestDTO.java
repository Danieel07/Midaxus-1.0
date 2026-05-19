package com.example.midaxus.model.dtos.auth;

/**
 * Data Transfer Object for ResetPasswordRequestDto.
 */
public class ResetPasswordRequestDto {
  private String token;
  private String newPassword;

  
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
   * Constructor for String getNewPassword.
   */
  
  /**
   * Gets the NewPassword.
   */
  public String getNewPassword() {
    return newPassword;
  }

  
  /**
   * Constructor for void setNewPassword.
   */
  
  /**
   * Sets the NewPassword.
   */
  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }
}






















