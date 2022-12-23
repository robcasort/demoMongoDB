package com.alca.service;

import java.util.List;
import java.util.Optional;

import com.alca.model.Persona;

public interface IPersonaService {
	public List<Persona> findAll();
	
	public Optional<Persona> findById(String id);
	
	public Persona save(Persona producto);
	
	public void deleteById(String id);
}
