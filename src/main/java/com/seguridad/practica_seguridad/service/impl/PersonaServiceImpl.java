package com.seguridad.practica_seguridad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seguridad.practica_seguridad.model.entities.PersonaEntity;
import com.seguridad.practica_seguridad.repository.PersonaRepository;
import com.seguridad.practica_seguridad.service.interfaces.IPersona;

import jakarta.transaction.Transactional;
@Service
public class PersonaServiceImpl implements IPersona{
    
    @Autowired
    PersonaRepository repository;

    @Override
    @Transactional
    public void savePersona(PersonaEntity persona) {
        this.repository.save(persona);
    }

    @Override
    public PersonaEntity findPersonaById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public List<PersonaEntity> findAllPersonas() {
        return this.repository.findAll();
    }

    @Override
    public void deletePersona(Long id) {
        this.repository.deleteById(id);
    }

    @Override
    @Transactional
    public void updatePersona(PersonaEntity persona) {
        if (this.repository.existsById(persona.getId())) {
            this.repository.save(persona);
        } 
    }
 
}
