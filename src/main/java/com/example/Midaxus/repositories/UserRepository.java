package com.example.midaxus.repositories;

import com.example.midaxus.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for UserRepository extends JpaRepository<User, String>.
 */
public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByEmail(String email);
  Optional<User> findByResetToken(String token);

}






















