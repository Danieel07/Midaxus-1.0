package com.example.midaxus.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import com.example.midaxus.model.dtos.InstitutionPolicyDto;
import com.example.midaxus.model.entities.InstitutionPolicy;
import com.example.midaxus.repositories.InstitutionPolicyRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InstitutionPolicyServiceTest {

  @Mock
  private InstitutionPolicyRepository repository;

  @InjectMocks
  private InstitutionPolicyService service;

  private InstitutionPolicy policy;

  @BeforeEach
  void setUp() {
    policy = new InstitutionPolicy();
    policy.setId(1L);
    policy.setStandardCapacity(40);
    policy.setCapacityTolerancePercent(10);
  }

  @Test
  void updatePolicy_WithValidValues_ShouldSucceed() {
    when(repository.findById(1L)).thenReturn(Optional.of(policy));
    when(repository.save(any(InstitutionPolicy.class))).thenAnswer(i -> i.getArguments()[0]);

    InstitutionPolicyDto dto = new InstitutionPolicyDto();
    dto.setStandardCapacity(50);
    dto.setCapacityTolerancePercent(20);

    InstitutionPolicyDto updated = service.updatePolicy(dto);

    assertEquals(50, updated.getStandardCapacity());
    assertEquals(20, updated.getCapacityTolerancePercent());
  }

  @Test
  void updatePolicy_WithInvalidCapacity_ShouldThrowException() {
    when(repository.findById(1L)).thenReturn(Optional.of(policy));

    InstitutionPolicyDto dto = new InstitutionPolicyDto();
    dto.setStandardCapacity(0);

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      service.updatePolicy(dto);
    });

    assertEquals("El aforo estándar debe ser mayor a 0", exception.getMessage());
  }

  @Test
  void updatePolicy_WithInvalidTolerance_ShouldThrowException() {
    when(repository.findById(1L)).thenReturn(Optional.of(policy));

    InstitutionPolicyDto dto = new InstitutionPolicyDto();
    dto.setCapacityTolerancePercent(-1);

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      service.updatePolicy(dto);
    });

    assertEquals("La tolerancia no puede ser negativa", exception.getMessage());
  }
}
