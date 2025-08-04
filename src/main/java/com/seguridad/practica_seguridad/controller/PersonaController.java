package com.seguridad.practica_seguridad.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import com.seguridad.practica_seguridad.model.dtos.AuthCreateUserRequest;
import com.seguridad.practica_seguridad.model.dtos.AuthResponse;
import com.seguridad.practica_seguridad.model.dtos.JwtResponse;
import com.seguridad.practica_seguridad.model.dtos.LoginRequest;
import com.seguridad.practica_seguridad.model.entities.PersonaEntity;
import com.seguridad.practica_seguridad.service.impl.PersonaServiceImpl;
import com.seguridad.practica_seguridad.service.userDetails.PersonaDetailServiceImpl;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RequestMapping("/persona")
@RestController
public class PersonaController {
  
    @Autowired
    PersonaServiceImpl service;
    @Autowired
    PersonaDetailServiceImpl details;

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthCreateUserRequest userRequest){
        return new ResponseEntity<>(this.details.createUser(userRequest), HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse>login(@RequestBody LoginRequest userRequest){
        return new ResponseEntity<>(this.details.loginUser(userRequest), HttpStatus.CREATED);
    }

    @GetMapping("/traer-todo")
    public List<PersonaEntity> getAllPersons() {
        return service.findAllPersonas();
    }
    @GetMapping("/traer-por-id/{id}")
    public PersonaEntity getPersonsById(@PathVariable Long id) {
        return service.findPersonaById(id);
    }
   
    @DeleteMapping("/delete-person/{id}")
    public void deletePerson(@PathVariable Long id) {
        service.deletePersona(id);
    }
   
    
}
