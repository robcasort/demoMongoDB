package com.alca.model;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Persona {

	@Id		
	private String id;
	private String nombre;
	private String apellidos;
	private Integer edad;
	private String email;
	
	public Persona(String nombre, String apellidos, Integer edad, String email) {
		super();
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.edad = edad;
		this.email = email;
	}
}
