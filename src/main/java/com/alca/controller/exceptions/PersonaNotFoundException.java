package com.alca.controller.exceptions;

public class PersonaNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1750600713669260627L;

	public PersonaNotFoundException(String id) {
		super("No se puede encontrar persona con id: " + id);
	}
}
