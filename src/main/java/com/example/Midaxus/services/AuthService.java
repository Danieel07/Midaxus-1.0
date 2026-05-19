package com.example.midaxus.services;

import com.example.midaxus.model.dtos.auth.ForgotPasswordRequestDto;
import com.example.midaxus.model.dtos.auth.LoginRequestDto;
import com.example.midaxus.model.dtos.auth.LoginResponseDto;
import com.example.midaxus.model.dtos.auth.ResetPasswordRequestDto;
import com.example.midaxus.model.entities.Admin;
import com.example.midaxus.model.entities.Student;
import com.example.midaxus.model.entities.Teacher;
import com.example.midaxus.model.entities.User;
import com.example.midaxus.model.mapper.AdminMapper;
import com.example.midaxus.model.mapper.StudentMapper;
import com.example.midaxus.model.mapper.TeacherMapper;
import com.example.midaxus.repositories.UserRepository;
import com.example.midaxus.util.JwtUtil;
import jakarta.mail.MessagingException;
import java.util.Date;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for authentication and password management.
 */
@Service
public class AuthService {

  private final UserRepository userRepository;
  private final EmailService emailService;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  /**
   * Constructs AuthService with required dependencies.
   */
  public AuthService(UserRepository userRepository, EmailService emailService,
      PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
    this.userRepository = userRepository;
    this.emailService = emailService;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
  }

  /**
   * Authenticates a user and generates a JWT token.
   *
   * @param login the login request credentials
   * @return a response containing the authentication status and token
   */
  public LoginResponseDto login(LoginRequestDto login) {
    User user = userRepository.findByEmail(login.getEmail()).orElse(null);

    if (user == null) {
      return new LoginResponseDto(false, "El correo no está registrado", null, null, null);
    }

    if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
      return new LoginResponseDto(false, "Contraseña incorrecta", null, null, null);
    }

    Object dto;
    String role;

    if (user instanceof Teacher t) {
      dto = TeacherMapper.toDto(t);
      role = "TEACHER";
    } else if (user instanceof Student s) {
      dto = StudentMapper.toDto(s);
      role = "STUDENT";
    } else if (user instanceof Admin a) {
      dto = AdminMapper.toDto(a);
      role = "ADMIN";
    } else {
      return new LoginResponseDto(false, "Tipo de usuario desconocido", null, null, null);
    }

    if (login.getExpectedRole() != null && !login.getExpectedRole().trim().isEmpty()) {
      if (!role.equalsIgnoreCase(login.getExpectedRole())) {
        String roleName = login.getExpectedRole().equalsIgnoreCase("TEACHER") ? "Profesor"
            : (login.getExpectedRole().equalsIgnoreCase("ADMIN") ? "Administrador" : "Estudiante");
        return new LoginResponseDto(false, "Credenciales no válidas para el panel de " + roleName,
            null, null, null);
      }
    }

    String token = jwtUtil.generateToken(user.getEmail(), role, user.getEmail(),
        user.getFirstName());

    return new LoginResponseDto(true, "Login exitoso", role, token, dto);
  }

  /**
   * Initiates the forgot password process by sending a reset email.
   *
   * @param request the request containing the user's email
   * @return true if the email was sent successfully, false otherwise
   */
  public boolean forgotPassword(ForgotPasswordRequestDto request) {
    User user = userRepository.findByEmail(request.getEmail()).orElse(null);
    if (user == null) {
      return false;
    }

    String token = UUID.randomUUID().toString();
    user.setResetToken(token);
    // Expiration in 1 hour
    user.setResetTokenExpiration(new Date(System.currentTimeMillis() + 3600000));
    userRepository.save(user);

    String resetUrl = "http://localhost:8088/reset-password?token=" + token;
    String htmlContent = "<h3>Recuperación de Contraseña</h3>"
        + "<p>Has solicitado restablecer tu contraseña en Midaxus.</p>"
        + "<p>Haz clic en el siguiente enlace para continuar:</p>"
        + "<a href='" + resetUrl + "'>Restablecer Contraseña</a>"
        + "<p>Si no solicitaste esto, ignora este correo.</p>";

    try {
      emailService.sendHtmlEmail(user.getEmail(), "Recuperación de Contraseña - Midaxus",
          htmlContent);
    } catch (MessagingException e) {
      System.err.println("Error enviando correo: " + e.getMessage());
      return false;
    }

    return true;
  }

  /**
   * Validates if a reset token is still valid.
   *
   * @param token the reset token to validate
   * @return true if the token is valid and not expired, false otherwise
   */
  public boolean validateResetToken(String token) {
    User user = userRepository.findByResetToken(token).orElse(null);
    if (user == null) {
      return false;
    }
    return !user.getResetTokenExpiration().before(new Date());
  }

  /**
   * Resets the user password using a valid reset token.
   *
   * @param request the request containing the token and new password
   * @return true if the password was reset successfully, false otherwise
   */
  public boolean resetPassword(ResetPasswordRequestDto request) {
    User user = userRepository.findByResetToken(request.getToken()).orElse(null);
    if (user == null) {
      return false;
    }

    if (user.getResetTokenExpiration().before(new Date())) {
      return false;
    }

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    user.setResetToken(null);
    user.setResetTokenExpiration(null);
    userRepository.save(user);
    return true;
  }
}

