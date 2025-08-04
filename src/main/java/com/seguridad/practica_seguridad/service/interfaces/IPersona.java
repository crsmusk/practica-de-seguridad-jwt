package com.seguridad.practica_seguridad.service.interfaces;

import java.util.List;
import com.seguridad.practica_seguridad.model.entities.PersonaEntity;

public interface IPersona {

    void savePersona(PersonaEntity persona);
    PersonaEntity findPersonaById(Long id);
    List<PersonaEntity> findAllPersonas();
    void deletePersona(Long id);
    void updatePersona(PersonaEntity persona);

}
