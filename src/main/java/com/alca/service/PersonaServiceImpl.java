package com.alca.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alca.controller.exceptions.PersonaNotFoundException;
import com.alca.model.Persona;
import com.alca.repository.PersonaRepository;

@Service
public class PersonaServiceImpl implements IPersonaService {
	private final PersonaRepository personaRepository;
	
	public PersonaServiceImpl(PersonaRepository personaRepository) {
		this.personaRepository = personaRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Persona> findAll() {
		return personaRepository.findAll();
	}

	@Override
	public Persona findById(String id) {
		return personaRepository.findById(id)
				.orElseThrow(() -> new PersonaNotFoundException(id));
	}

	@Override
	@Transactional
	public Persona save(Persona persona) {
		return personaRepository.save(persona);
	}

	@Override
	@Transactional
	public void deleteById(String id) {
		personaRepository.deleteById(id);		
	}	
}
