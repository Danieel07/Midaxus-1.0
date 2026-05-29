package com.example.midaxus.services;

/**
 * Interface for User service operations.
 *
 * @param <T> the DTO type
 * @param <ID> the ID type
 */
public interface IUser<T, ID> {

  /**
   * Creates a new user.
   *
   * @param t the user DTO to create
   * @return the created user DTO
   */
  T createUser(T t);

  /**
   * Deletes a user by its ID.
   *
   * @param id the ID of the user to delete
   */
  void deleteUser(ID id);

  /**
   * Retrieves a user by its ID.
   *
   * @param id the ID of the user
   * @return the user DTO
   */
  T getUser(ID id);

  /**
   * Updates an existing user.
   *
   * @param id the ID of the user to update
   * @param t the user DTO with updated information
   * @return the updated user DTO
   */
  T updateUser(ID id, T t);
}

