package com.example.midaxus.controller;

import com.example.midaxus.model.dtos.auth.ForgotPasswordRequestDto;
import com.example.midaxus.model.dtos.auth.LoginRequestDto;
import com.example.midaxus.model.dtos.auth.LoginResponseDto;
import com.example.midaxus.model.dtos.auth.ResetPasswordRequestDto;
import com.example.midaxus.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for AuthController.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  /**
   * Constructor for AuthController.
   *
   * @param authService the authentication service.
   */
  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  /**
   * Handles user login.
   *
   * @param request the login request DTO.
   * @return the response entity with the login response.
   */
  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
    return ResponseEntity.ok(authService.login(request));
  }

  /**
   * Handles forgot password request.
   *
   * @param request the forgot password request DTO.
   * @return the response entity with the status message.
   */
  @PostMapping("/forgot-password")
  public ResponseEntity<LoginResponseDto> forgotPassword(
      @RequestBody ForgotPasswordRequestDto request) {
    boolean success = authService.forgotPassword(request);
    if (success) {
      return ResponseEntity.ok(new LoginResponseDto(true,
          "Si el correo existe, se ha enviado un enlace de recuperación.",
          null, null, null));
    } else {
      return ResponseEntity.ok(new LoginResponseDto(false,
          "No se pudo procesar la solicitud para este correo.",
          null, null, null));
    }
  }

  /**
   * Validates a password reset token.
   *
   * @param token the reset token.
   * @return the response entity with the validation status.
   */
  @GetMapping("/validate-reset-token")
  public ResponseEntity<LoginResponseDto> validateResetToken(@RequestParam String token) {
    boolean valid = authService.validateResetToken(token);
    if (valid) {
      return ResponseEntity.ok(new LoginResponseDto(true, "Token válido", null, null, null));
    } else {
      return ResponseEntity.ok(new LoginResponseDto(false, "Token inválido o expirado",
          null, null, null));
    }
  }

  /**
   * Handles password reset.
   *
   * @param request the reset password request DTO.
   * @return the response entity with the reset status.
   */
  @PostMapping("/reset-password")
  public ResponseEntity<LoginResponseDto> resetPassword(
      @RequestBody ResetPasswordRequestDto request) {
    boolean success = authService.resetPassword(request);
    if (success) {
      return ResponseEntity.ok(new LoginResponseDto(true, "Contraseña restablecida con éxito.",
          null, null, null));
    } else {
      return ResponseEntity.ok(new LoginResponseDto(false, "Token inválido o expirado.",
          null, null, null));
    }
  }
}

