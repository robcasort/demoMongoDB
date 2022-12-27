package com.alca.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.alca.controller.hateoas.PersonaModelAssembler;
import com.alca.dto.PersonaRequest;
import com.alca.dto.PersonaResponse;
import com.alca.model.Persona;
import com.alca.service.IPersonaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
//@RequestMapping("apipersonas")
public class PersonaController {

	private final IPersonaService personaService;
	private final PersonaModelAssembler assembler;

	public PersonaController(IPersonaService personaService, PersonaModelAssembler assembler) {
		this.personaService = personaService;
		this.assembler = assembler;
	}

	@Operation(summary = "Obtener lista de personas", 
			description = "Obtener lista de personas", 
			tags = {"apipersonas"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
				description = "Personas encontradas", 
				content = {@Content(mediaType = "application/json", 
				schema = @Schema(implementation = PersonaResponse.class)) }) 
	})
	@GetMapping("/personas")
	@ResponseStatus(HttpStatus.OK)	
	public CollectionModel<EntityModel<PersonaResponse>> listarTodos() {
		// Devolver lista de personas 
		List<PersonaResponse> personas = personaService.findAll()
				.stream()
				.map(PersonaResponse::new)
				.collect(Collectors.toList());				
				
		// Formatear la respueta
		List<EntityModel<PersonaResponse>> personasResponse = personas.stream() 
				.map(assembler::toModel) 
				.collect(Collectors.toList());

		return assembler.toModel(personasResponse);
	}

	@Operation(summary = "Obtener una persona por Id", 
			description = "Obtener una persona por Id", 
			tags = {"apipersonas"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
				description = "Persona encontrada", 
				content = {@Content(mediaType = "application/json", 
				schema = @Schema(implementation = PersonaResponse.class)) }),
			@ApiResponse(responseCode = "400", 
				description = "Id de persona suministrado no valido", 
				content = @Content),
			@ApiResponse(responseCode = "404", 
				description = "Persona no encontrada", 
				content = @Content) 
	})
	@GetMapping("/personas/{id}")
	@ResponseStatus(HttpStatus.OK)
	public EntityModel<PersonaResponse> verPersona(@PathVariable String id) {	
		PersonaResponse persona = new PersonaResponse(personaService.findById(id));

		return assembler.toModel(persona);
	}

	@Operation(summary = "Agregar una persona",
			tags = {"apipersonas"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
            	description = "Detalle de persona agregada",
            	content = {@Content(mediaType = "application/json",
            	schema = @Schema(implementation = PersonaResponse.class))}),
            @ApiResponse(responseCode = "404",
            description = "Persona no encontrada",
            content = @Content)
    })
	@PostMapping("/personas")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> nuevaPersona(@RequestBody PersonaRequest newPersona) {
		// Usar el patron builder para obtener un objeto Persona
		Persona persona = Persona.builder()
			.nombre(newPersona.getNombre())
			.apellidos(newPersona.getApellidos())
			.edad(newPersona.getEdad())
			.email(newPersona.getEmail())
			.build();
		
		// Formatear la respuesta
		EntityModel<PersonaResponse> entityModel = assembler.toModel(
				new PersonaResponse(personaService.save(persona)));

		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.body(entityModel);
	}

	@Operation(summary = "Actualizar informacion de una persona por Id",
			tags = {"apipersonas"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
            	description = "Detalle de la persona actualizada",
            	content = {@Content(mediaType = "application/json",
            	schema = @Schema(implementation = PersonaResponse.class))}),
            @ApiResponse(responseCode = "400", 
        		description = "Id de persona suministrado no valido", 
        		content = @Content),
            @ApiResponse(responseCode = "404",
            	description = "Persona no encontrada",
            	content = @Content)
    })
	@PutMapping("/personas/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> actualizarPersona(@RequestBody PersonaRequest updatedPersona, @PathVariable String id) {		
		Persona personaSave = personaService.findById(id);
		personaSave.setId(id);
		personaSave.setNombre(updatedPersona.getNombre());
		personaSave.setApellidos(updatedPersona.getApellidos());
		personaSave.setEdad(updatedPersona.getEdad());
		personaSave.setEmail(updatedPersona.getEmail());

		// Actualizar persona y formatear la respuesta
		PersonaResponse persona = new PersonaResponse(personaService.save(personaSave));		

		EntityModel<PersonaResponse> entityModel = assembler.toModel(persona);

		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}

	@Operation(summary = "Borrar una persona por Id",
			tags = {"apipersonas"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
            	description = "Persona borrada"),
            @ApiResponse(responseCode = "400", 
            	description = "Id de persona suministrado no valido", 
            	content = @Content),
            @ApiResponse(responseCode = "404",
            	description = "Persona no encontrada",
            	content = @Content)
    })
	@DeleteMapping("/personas/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> borrarPersona(@PathVariable String id) {
		personaService.deleteById(id);

		return ResponseEntity.noContent().build();
	}

}
