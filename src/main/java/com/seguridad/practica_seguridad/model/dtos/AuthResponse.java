package com.seguridad.practica_seguridad.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username","message","jwt","status"})
public record AuthResponse (String username,
                           String message,
                           String jwt,
                           boolean status){

}
