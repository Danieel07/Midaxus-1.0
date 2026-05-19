package com.example.midaxus.services;

import java.util.List;

/**
 * Interface for Student service operations.
 *
 * @param <K> the ID type
 * @param <J> the DTO type
 */
public interface IStudent<K, J> {

  /**
   * Creates a new student.
   *
   * @param j the student DTO to create
   * @return the created student DTO
   */
  J createStudent(J j);

  /**
   * Retrieves a student by its ID.
   *
   * @param k the ID of the student
   * @return the student DTO
   */
  J getStudent(K k);

  /**
   * Deletes a student.
   *
   * @param j the student DTO to delete
   */
  void deleteStudent(J j);

  /**
   * Retrieves all students.
   *
   * @return a list of all student DTOs
   */
  List<J> getStudents();
}

