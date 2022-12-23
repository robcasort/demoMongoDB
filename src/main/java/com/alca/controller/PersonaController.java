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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.alca.controller.exceptions.PersonaNotFoundException;
import com.alca.controller.hateoas.PersonaModelAssembler;
import com.alca.model.Persona;
import com.alca.service.IPersonaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("apipersonas")
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
					schema = @Schema(implementation = Persona.class)) }) 
	})
	@GetMapping("/personas")
	public CollectionModel<EntityModel<Persona>> listarTodos() {
		List<EntityModel<Persona>> personas = personaService.findAll().stream() //
				.map(assembler::toModel) //
				.collect(Collectors.toList());

		return assembler.toModel(personas);
	}

	@Operation(summary = "Obtener una persona por Id", 
			description = "Obtener una persona por Id", 
			tags = {"apipersonas"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
				description = "Persona encontrada", 
				content = {@Content(mediaType = "application/json", 
					schema = @Schema(implementation = Persona.class)) }),
			@ApiResponse(responseCode = "400", 
				description = "Id de persona suministrado no valido", 
				content = @Content),
			@ApiResponse(responseCode = "404", 
				description = "Persona no encontrada", 
				content = @Content) 
	})
	@GetMapping("/personas/{id}")
	public EntityModel<Persona> verPersona(@PathVariable String id) {
		Persona persona = personaService.findById(id).orElseThrow(() -> new PersonaNotFoundException(id));

		return assembler.toModel(persona);
	}

	@Operation(summary = "Agregar una persona",
			tags = {"apipersonas"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Detalle de persona agregada",
            content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
            description = "Persona no encontrada",
            content = @Content)
    })
	@PostMapping("/personas")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> nuevaPersona(@RequestBody Persona newPersona) {
		EntityModel<Persona> entityModel = assembler.toModel(personaService.save(newPersona));

		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.body(entityModel);
	}

	@Operation(summary = "Actualizar informacion de una persona por Id",
			tags = {"apipersonas"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            	description = "Detalle de la persona actualizada",
            	content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", 
        		description = "Id de persona suministrado no valido", 
        		content = @Content),
            @ApiResponse(responseCode = "404",
            	description = "Persona no encontrada",
            	content = @Content)
    })
	@PutMapping("/personas/{id}")
	public ResponseEntity<?> actualizarPersona(@RequestBody Persona newPersona, @PathVariable String id) {
		Persona updatedPersona = personaService.findById(id).map(p -> {
			p.setNombre(newPersona.getNombre());
			p.setApellidos(newPersona.getApellidos());
			p.setEdad(newPersona.getEdad());
			p.setEmail(newPersona.getEmail());
			return personaService.save(p);
		}).orElseGet(() -> {
			return personaService.save(newPersona);
		});

		EntityModel<Persona> entityModel = assembler.toModel(updatedPersona);

		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}

	@Operation(summary = "Borrar una persona por Id",
			tags = {"apipersonas"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            	description = "Persona borrada",
            		content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", 
            	description = "Id de persona suministrado no valido", 
            	content = @Content),
            @ApiResponse(responseCode = "404",
            	description = "Persona no encontrada",
            	content = @Content)
    })
	@DeleteMapping("/personas/{id}")
	public ResponseEntity<?> borrarPersona(@PathVariable String id) {
		personaService.deleteById(id);

		return ResponseEntity.noContent().build();
	}

}
