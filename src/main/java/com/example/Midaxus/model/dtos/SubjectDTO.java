package com.example.midaxus.model.dtos;

/**
 * Data Transfer Object for SubjectDto.
 */
public class SubjectDto {

  private String idSubject;
  private String subjectName;
  private int sessionPerWeek;
  private int durationMinutes;


  
  /**
   * Constructor for SubjectDto.
   */
  public SubjectDto() {}

  public SubjectDto(String idSubject, String subjectName, int sessionPerWeek, int durationMinutes) {
    this.idSubject = idSubject;
    this.subjectName = subjectName;
    this.sessionPerWeek = sessionPerWeek;
    this.durationMinutes = durationMinutes;
  }

  
  /**
   * Constructor for String getIdSubject.
   */
  
  /**
   * Gets the IdSubject.
   */
  public String getIdSubject() {
    return idSubject;
  }

  
  /**
   * Constructor for void setIdSubject.
   */
  
  /**
   * Sets the IdSubject.
   */
  public void setIdSubject(String idSubject) {
    this.idSubject = idSubject;
  }

  
  /**
   * Constructor for String getSubjectName.
   */
  
  /**
   * Gets the SubjectName.
   */
  public String getSubjectName() {
    return subjectName;
  }

  
  /**
   * Constructor for void setSubjectName.
   */
  
  /**
   * Sets the SubjectName.
   */
  public void setSubjectName(String subjectName) {
    this.subjectName = subjectName;
  }

  
  /**
   * Constructor for int getSessionPerWeek.
   */
  
  /**
   * Gets the SessionPerWeek.
   */
  public int getSessionPerWeek() {
    return sessionPerWeek;
  }

  
  /**
   * Constructor for void setSessionPerWeek.
   */
  
  /**
   * Sets the SessionPerWeek.
   */
  public void setSessionPerWeek(int sessionPerWeek) {
    this.sessionPerWeek = sessionPerWeek;
  }

  
  /**
   * Constructor for int getDurationMinutes.
   */
  
  /**
   * Gets the DurationMinutes.
   */
  public int getDurationMinutes() {
    return durationMinutes;
  }

  
  /**
   * Constructor for void setDurationMinutes.
   */
  
  /**
   * Sets the DurationMinutes.
   */
  public void setDurationMinutes(int durationMinutes) {
    this.durationMinutes = durationMinutes;
  }
}






















