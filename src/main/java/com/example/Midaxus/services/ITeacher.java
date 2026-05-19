package com.example.midaxus.services;

import java.util.List;

/**
 * Interface for Teacher service operations.
 *
 * @param <T> the DTO type
 * @param <ID> the ID type
 */
public interface ITeacher<T, ID> {

  /**
   * Creates a new teacher.
   *
   * @param dto the teacher DTO to create
   * @return the created teacher DTO
   */
  T createTeacher(T dto);

  /**
   * Updates an existing teacher.
   *
   * @param id the ID of the teacher to update
   * @param dto the updated teacher data
   * @return the updated teacher DTO
   */
  T updateTeacher(ID id, T dto);

  /**
   * Deletes a teacher by its ID.
   *
   * @param id the ID of the teacher to delete
   */
  void deleteTeacher(ID id);

  /**
   * Retrieves a teacher by its ID.
   *
   * @param id the ID of the teacher
   * @return the teacher DTO
   */
  T getTeacher(ID id);

  /**
   * Retrieves all teachers.
   *
   * @return a list of all teacher DTOs
   */
  List<T> getTeachers();
}

