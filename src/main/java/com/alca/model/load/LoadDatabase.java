package com.alca.model.load;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alca.model.Persona;
import com.alca.repository.PersonaRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class LoadDatabase {
	@Bean
	CommandLineRunner initDatabase(PersonaRepository repository) {		
		repository.deleteAll();
		
		return args -> {
			log.info("Registrando " + repository.save(new Persona("Juan", "Perez", 33, "jprez@gmail.com")));
			log.info("Registrando " + repository.save(new Persona("Andrea", "Garcia", 42, "agarcia@gmail.com")));
			log.info("Registrando " + repository.save(new Persona("Maria", "Cardenas", 28, "agarcia@gmail.com")));
			log.info("Registrando " + repository.save(new Persona("Julio", "Miranda", 35, "agarcia@gmail.com")));
			
			log.info("Buscar por nombre exacto 1: " + repository.buscarPorNombreExacto("Juan"));
			log.info("Buscar por nombre exacto 2: " + repository.findByNombre("Juan"));
			
			log.info("Buscar por apellidos exacto 1: " + repository.buscarPorApellidosExacto("Miranda"));
			log.info("Buscar por apellidos exacto 2: " + repository.findByApellidos("Miranda")); 
						
			log.info("Buscar por NOT nombre: " + repository.findByNombreNot("Juan"));
			log.info("Buscar por NOT apellidos " + repository.findByApellidosNot("Miranda"));
			
			log.info("Buscar por nombre regex: " + repository.findByNombreRegex("ua"));
			log.info("Buscar por nombre like: " + repository.findByNombreLike("ua"));
			log.info("Buscar por nombre like 2: " + repository.findByNombreLike("UA"));
			log.info("Buscar por nombre por patron (case sensitive) 1: " + repository.buscarPorNombreConPatronCaseSentive("ua"));
			log.info("Buscar por nombre por patron (case sensitive) 2: " + repository.buscarPorNombreConPatronCaseSentive("UA"));
			log.info("Buscar por nombre por patron (case insensitive) 3: " + repository.buscarPorNombreConPatronCaseInsensitive("UA"));
			log.info("Buscar por nombre por patron (case insensitive) 4: " + repository.buscarPorNombreConPatronCaseInsensitive("UA"));
			
			log.info("Buscar por apellidos regex: " + repository.findByApellidosRegex("irand"));
			log.info("Buscar por apellidos like: " + repository.findByApellidosLike("iran"));		
		};
	}
}
