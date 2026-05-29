package com.example.midaxus.controller;

import com.example.midaxus.model.dtos.UserDto;
import com.example.midaxus.services.IUser;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for managing users.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private IUser<UserDto, String> userRepos;

  /**
   * Creates a new user. Only admins can perform this action.
   *
   * @param dto the user DTO to create
   * @return a ResponseEntity containing the created user DTO
   */
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserDto> createUser(@RequestBody UserDto dto) {
    UserDto created = userRepos.createUser(dto);
    return ResponseEntity
      .created(URI.create("/api/users")).body(created);
  }

  /**
   * Deletes a user by their ID. Only admins can perform this action.
   *
   * @param id the ID of the user to delete
   * @return a ResponseEntity with no content
   */
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteUser(@PathVariable String id) {
    userRepos.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Updates an existing user. Only admins can perform this action.
   *
   * @param id the ID of the user to update
   * @param dto the user DTO with updated information
   * @return a ResponseEntity containing the updated user DTO
   */
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserDto> updateUser(
      @PathVariable String id, @RequestBody UserDto dto) {
    return ResponseEntity.ok(userRepos.updateUser(id, dto));
  }
}

