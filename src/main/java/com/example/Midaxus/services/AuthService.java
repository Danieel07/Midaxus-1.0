package com.example.Midaxus.services;

import com.example.Midaxus.model.dtos.auth.ForgotPasswordRequestDTO;
import com.example.Midaxus.model.dtos.auth.LoginRequestDTO;
import com.example.Midaxus.model.dtos.auth.LoginResponseDTO;
import com.example.Midaxus.model.dtos.auth.ResetPasswordRequestDTO;
import com.example.Midaxus.model.entities.Admin;
import com.example.Midaxus.model.entities.Student;
import com.example.Midaxus.model.entities.Teacher;
import com.example.Midaxus.model.entities.User;
import com.example.Midaxus.model.mapper.AdminMapper;
import com.example.Midaxus.model.mapper.StudentMapper;
import com.example.Midaxus.model.mapper.TeacherMapper;
import com.example.Midaxus.repositories.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Midaxus.util.JwtUtil;

import java.util.Date;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // -----------------------
    // LOGIN
    // -----------------------
    public LoginResponseDTO login(LoginRequestDTO login) {

        User user = userRepository.findByEmail(login.getEmail()).orElse(null);

        if (user == null) {
            return new LoginResponseDTO(false, "El correo no está registrado", null, null, null);
        }

        if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            return new LoginResponseDTO(false, "Contraseña incorrecta", null, null, null);
        }

        Object dto;
        String role;

        if (user instanceof Teacher t) {
            dto = TeacherMapper.toDTO(t);
            role = "TEACHER";

        } else if (user instanceof Student s) {
            dto = StudentMapper.toDTO(s);
            role = "STUDENT";

        } else if (user instanceof Admin a) {
            dto = AdminMapper.toDTO(a);
            role = "ADMIN";

        } else {
            return new LoginResponseDTO(false, "Tipo de usuario desconocido", null, null, null);
        }

        if (login.getExpectedRole() != null && !login.getExpectedRole().trim().isEmpty()) {
            if (!role.equalsIgnoreCase(login.getExpectedRole())) {
                String roleName = login.getExpectedRole().equalsIgnoreCase("TEACHER") ? "Profesor" : 
                                  (login.getExpectedRole().equalsIgnoreCase("ADMIN") ? "Administrador" : "Estudiante");
                return new LoginResponseDTO(false, "Credenciales no válidas para el panel de " + roleName, null, null, null);
            }
        }

        String token = jwtUtil.generateToken(user.getEmail(), role, user.getEmail(), user.getFirstName());

        return new LoginResponseDTO(true, "Login exitoso", role, token, dto);
    }

    public boolean forgotPassword(ForgotPasswordRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null) return false;

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        // Expiration in 1 hour
        user.setResetTokenExpiration(new Date(System.currentTimeMillis() + 3600000));
        userRepository.save(user);

        String resetUrl = "http://localhost:8080/reset-password?token=" + token;
        String htmlContent = "<h3>Recuperación de Contraseña</h3>" +
                "<p>Has solicitado restablecer tu contraseña en Midaxus.</p>" +
                "<p>Haz clic en el siguiente enlace para continuar:</p>" +
                "<a href='" + resetUrl + "'>Restablecer Contraseña</a>" +
                "<p>Si no solicitaste esto, ignora este correo.</p>";

        try {
            emailService.sendHtmlEmail(user.getEmail(), "Recuperación de Contraseña - Midaxus", htmlContent);
        } catch (MessagingException e) {
            System.err.println("Error enviando correo: " + e.getMessage());
            return false;
        }

        return true;
    }

    public boolean validateResetToken(String token) {
        User user = userRepository.findByResetToken(token).orElse(null);
        if (user == null) return false;
        return !user.getResetTokenExpiration().before(new Date());
    }

    public boolean resetPassword(ResetPasswordRequestDTO request) {
        User user = userRepository.findByResetToken(request.getToken()).orElse(null);
        if (user == null) return false;

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

