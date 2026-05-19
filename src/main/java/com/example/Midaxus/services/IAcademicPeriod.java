package com.example.midaxus.services;

import java.util.List;

/**
 * Interface for Academic Period service operations.
 *
 * @param <T> the DTO type
 * @param <K> the ID type
 */
public interface IAcademicPeriod<T, K> {

  /**
   * Creates a new academic period.
   *
   * @param dto the academic period DTO to create
   * @return the created academic period DTO
   */
  T create(T dto);

  /**
   * Retrieves an academic period by its ID.
   *
   * @param id the ID of the academic period
   * @return the academic period DTO
   */
  T getById(K id);

  /**
   * Retrieves all academic periods.
   *
   * @return a list of all academic period DTOs
   */
  List<T> getAll();

  /**
   * Deletes an academic period by its ID.
   *
   * @param id the ID of the academic period to delete
   */
  void delete(K id);
}

