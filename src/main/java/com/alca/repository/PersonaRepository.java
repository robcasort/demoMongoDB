package com.alca.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.alca.model.Persona;


// https://docs.spring.io/spring-data/mongodb/docs/1.2.0.RELEASE/reference/html/mongo.repositories.html
@Repository
public interface PersonaRepository extends MongoRepository<Persona, String> {
	List<Persona> findByNombre(String nombre);
	List<Persona> findByApellidos(String apellidos);
	
	List<Persona> findByNombreNot(String nombre);
	List<Persona> findByApellidosNot(String apellidos);
	
	List<Persona> findByNombreLike(String nombre);
	List<Persona> findByApellidosLike(String apellidos);
	
	// (Case sensitive)
	List<Persona> findByNombreRegex(String nombre);
	List<Persona> findByApellidosRegex(String apellidos);
	
	@Query("{'nombre' : ?0}")	
	List<Persona> buscarPorNombreExacto(String firstname);
	
	@Query("{'apellidos' : ?0}")
	List<Persona> buscarPorApellidosExacto(String apellidos);
		
	// (Case sensitive)
	@Query("{'nombre': {$regex : ?0}})")
    List<Persona> buscarPorNombreConPatronCaseSentive(String nombre);
	
	// Case insensitive
	@Query("{ 'nombre' : {'$regex' : ?0 , $options: 'i'}}")
    List<Persona> buscarPorNombreConPatronCaseInsensitive(String patronNombre);
}
