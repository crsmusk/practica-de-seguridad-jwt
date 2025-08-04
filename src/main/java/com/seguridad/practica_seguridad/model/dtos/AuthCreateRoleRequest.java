package com.seguridad.practica_seguridad.model.dtos;

import java.util.List;

import jakarta.validation.constraints.Size;

public record AuthCreateRoleRequest(@Size(max = 3,message = "the user  cannot more than three role") List<String>roleListName) {}