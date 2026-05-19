package com.example.midaxus.services;

import java.util.List;

/**
 * Interface for Subject service operations.
 *
 * @param <T> the DTO type
 * @param <K> the ID type
 */
public interface ISubject<T, K> {

  /**
   * Creates a new subject.
   *
   * @param t the subject DTO to create
   * @return the created subject DTO
   */
  T create(T t);

  /**
   * Deletes a subject by its ID.
   *
   * @param id the ID of the subject to delete
   */
  void delete(K id);

  /**
   * Retrieves all subjects.
   *
   * @return a list of all subject DTOs
   */
  List<T> findAll();

  /**
   * Retrieves a subject by its ID.
   *
   * @param id the ID of the subject
   * @return the subject DTO
   */
  T findById(K id);
}

