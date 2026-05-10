package com.example.Midaxus.controller;

import com.example.Midaxus.model.dtos.auth.ForgotPasswordRequestDTO;
import com.example.Midaxus.model.dtos.auth.LoginRequestDTO;
import com.example.Midaxus.model.dtos.auth.LoginResponseDTO;
import com.example.Midaxus.model.dtos.auth.ResetPasswordRequestDTO;
import com.example.Midaxus.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // ✅ Constructor único
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ✅ LOGIN (único endpoint)
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<LoginResponseDTO> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        boolean success = authService.forgotPassword(request);
        if (success) {
            return ResponseEntity.ok(new LoginResponseDTO(true, "Si el correo existe, se ha enviado un enlace de recuperación.", null, null, null));
        } else {
            return ResponseEntity.ok(new LoginResponseDTO(false, "No se pudo procesar la solicitud para este correo.", null, null, null));
        }
    }

    @GetMapping("/validate-reset-token")
    public ResponseEntity<LoginResponseDTO> validateResetToken(@RequestParam String token) {
        boolean valid = authService.validateResetToken(token);
        if (valid) {
            return ResponseEntity.ok(new LoginResponseDTO(true, "Token válido", null, null, null));
        } else {
            return ResponseEntity.ok(new LoginResponseDTO(false, "Token inválido o expirado", null, null, null));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<LoginResponseDTO> resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        boolean success = authService.resetPassword(request);
        if (success) {
            return ResponseEntity.ok(new LoginResponseDTO(true, "Contraseña restablecida con éxito.", null, null, null));
        } else {
            return ResponseEntity.ok(new LoginResponseDTO(false, "Token inválido o expirado.", null, null, null));
        }
    }

}