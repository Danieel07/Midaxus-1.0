package com.example.midaxus.model.dtos;

import java.util.List;

/**
 * Data Transfer Object for ApiResponse<T>.
 */
public class ApiResponse<T> {
  private T data;
  private String message;
  private List<String> warnings;

  
  /**
   * Constructor for ApiResponse.
   */
  public ApiResponse() {
  }

  
  /**
   * Constructor for ApiResponse.
   */
  public ApiResponse(T data, String message, List<String> warnings) {
    this.data = data;
    this.message = message;
    this.warnings = warnings;
  }

  
  /**
   * Constructor for T getData.
   */
  
  /**
   * Gets the Data.
   */
  public T getData() {
    return data;
  }

  
  /**
   * Constructor for void setData.
   */
  
  /**
   * Sets the Data.
   */
  public void setData(T data) {
    this.data = data;
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
   * Constructor for List<String> getWarnings.
   */
  
  /**
   * Gets the Warnings.
   */
  public List<String> getWarnings() {
    return warnings;
  }

  
  /**
   * Constructor for void setWarnings.
   */
  
  /**
   * Sets the Warnings.
   */
  public void setWarnings(List<String> warnings) {
    this.warnings = warnings;
  }
}






















