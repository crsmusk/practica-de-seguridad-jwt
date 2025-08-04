package com.seguridad.practica_seguridad.model.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record AuthCreateUserRequest(@NotBlank String username,
                                    @NotBlank String surname,
                                    @NotBlank String email,
                                    @NotBlank String password,
                                    @Valid AuthCreateRoleRequest roleRequest) {}