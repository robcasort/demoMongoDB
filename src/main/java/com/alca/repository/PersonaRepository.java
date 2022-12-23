package com.alca.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.alca.model.Persona;

public interface PersonaRepository extends MongoRepository<Persona, String> {
	
}
