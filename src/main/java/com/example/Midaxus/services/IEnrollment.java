package com.example.midaxus.services;

import java.util.List;

/**
 * Interface for Enrollment service operations.
 *
 * @param <T> the DTO type
 * @param <ID> the ID type
 */
public interface IEnrollment<T, ID> {

  /**
   * Creates a new enrollment.
   *
   * @param t the enrollment DTO to create
   * @return the created enrollment DTO
   */
  T createEnrollment(T t);

  /**
   * Retrieves an enrollment by its ID.
   *
   * @param id the ID of the enrollment
   * @return the enrollment DTO
   */
  T getEnrollment(ID id);

  /**
   * Retrieves all enrollments.
   *
   * @return a list of all enrollment DTOs
   */
  List<T> getAll();

  /**
   * Deletes an enrollment by its ID.
   *
   * @param id the ID of the enrollment to delete
   */
  void deleteEnrollment(ID id);
}

