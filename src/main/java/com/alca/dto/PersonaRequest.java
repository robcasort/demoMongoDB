package com.alca.dto;

import com.alca.model.Persona;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonaRequest {	
	private String nombre;
	private String apellidos;
	private Integer edad;
	private String email;
	
	public PersonaRequest (Persona persona) {	
		super();
		this.nombre = persona.getNombre();
		this.apellidos = persona.getApellidos();
		this.edad = persona.getEdad();
		this.email = persona.getEmail();
	}
}
