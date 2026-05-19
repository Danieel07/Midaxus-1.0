package com.example.midaxus.services;

import java.util.List;

/**
 * Interface for Course Group service operations.
 *
 * @param <T> the DTO type
 * @param <K> the ID type
 */
public interface ICourseGroup<T, K> {

  /**
   * Creates a new course group.
   *
   * @param dto the course group DTO to create
   * @return the created course group DTO
   */
  T create(T dto);

  /**
   * Updates an existing course group.
   *
   * @param id the ID of the course group to update
   * @param dto the updated course group data
   * @return the updated course group DTO
   */
  T update(K id, T dto);

  /**
   * Retrieves a course group by its ID.
   *
   * @param id the ID of the course group
   * @return the course group DTO
   */
  T getById(K id);

  /**
   * Retrieves all course groups.
   *
   * @return a list of all course group DTOs
   */
  List<T> getAll();

  /**
   * Deletes a course group by its ID.
   *
   * @param id the ID of the course group to delete
   */
  void delete(K id);

  /**
   * Retrieves course groups assigned to a specific teacher.
   *
   * @param teacherId the ID of the teacher
   * @return a list of course group DTOs
   */
  List<T> getByTeacher(K teacherId);

  /**
   * Retrieves course groups for a specific subject.
   *
   * @param subjectId the ID of the subject
   * @return a list of course group DTOs
   */
  List<T> getBySubject(K subjectId);

  /**
   * Retrieves courses taught by a specific teacher.
   *
   * @param teacherId the ID of the teacher
   * @return a list of course group DTOs
   */
  List<T> getCoursesByTeacher(K teacherId);

  /**
   * Retrieves courses taken by a specific student.
   *
   * @param studentId the ID of the student
   * @return a list of course group DTOs
   */
  List<T> getCoursesByStudent(K studentId);
}

