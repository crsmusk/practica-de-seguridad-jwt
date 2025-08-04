package com.seguridad.practica_seguridad.model.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public record LoginRequest (@NotBlank String username,
                            @NotBlank String password) {
} 

