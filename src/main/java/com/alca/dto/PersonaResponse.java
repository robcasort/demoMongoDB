package com.alca.dto;

import com.alca.model.Persona;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonaResponse {
	private String id;
	private String nombre;
	private String apellidos;
	private Integer edad;
	private String email;
	
	public PersonaResponse (Persona persona) {
		super();
		this.id = persona.getId();
		this.nombre = persona.getNombre();
		this.apellidos = persona.getApellidos();
		this.edad = persona.getEdad();
		this.email = persona.getEmail();
	}
}
