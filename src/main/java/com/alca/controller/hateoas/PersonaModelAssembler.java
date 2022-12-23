package com.alca.controller.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.alca.controller.PersonaController;
import com.alca.model.Persona;

@Component
public class PersonaModelAssembler implements RepresentationModelAssembler<Persona, EntityModel<Persona>> {

	  @Override
	  public EntityModel<Persona> toModel(Persona persona) {

	    return EntityModel.of(persona, 
	        linkTo(methodOn(PersonaController.class).verPersona(persona.getId())).withSelfRel(),
	        linkTo(methodOn(PersonaController.class).listarTodos()).withRel("personas"));
	  }
	  	  
	  public CollectionModel<EntityModel<Persona>> toModel(List<EntityModel<Persona>> personas) {
		  return CollectionModel.of(personas, linkTo(methodOn(PersonaController.class).listarTodos()).withSelfRel());
	  }
}
