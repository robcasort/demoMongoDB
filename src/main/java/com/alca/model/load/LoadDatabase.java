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
		};
	}
}
