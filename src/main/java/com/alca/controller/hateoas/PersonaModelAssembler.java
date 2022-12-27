package com.alca.controller.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.alca.controller.PersonaController;
import com.alca.dto.PersonaResponse;

@Component
public class PersonaModelAssembler implements RepresentationModelAssembler<PersonaResponse, EntityModel<PersonaResponse>> {

	@Override
	public EntityModel<PersonaResponse> toModel(PersonaResponse persona) {
		return EntityModel.of(persona,
				linkTo(methodOn(PersonaController.class).verPersona(persona.getId())).withSelfRel(),
				linkTo(methodOn(PersonaController.class).listarTodos()).withRel("personas"));
	}
	
	public CollectionModel<EntityModel<PersonaResponse>> toModel(List<EntityModel<PersonaResponse>> personas) {
		return CollectionModel.of(personas, linkTo(methodOn(PersonaController.class).listarTodos()).withSelfRel());
	}
}
