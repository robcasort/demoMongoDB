package com.alca.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alca.model.Persona;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class PersonaRepositoryTest {

	// de.flapdoodle.embed.mongo.distribution.Version.V4_0_12;
	// de.flapdoodle.embed.mongo.distribution.Version.V4_0_2;
	// de.flapdoodle.embed.mongo.distribution.Version.V4_4_1;

	@Autowired
	private PersonaRepository personaRepository;

	private static List<Persona> personas;

	@BeforeAll
	static void beforeAll() {		
		try {
			String jsonString = new String(new ClassPathResource("persona-data.json").getInputStream().readAllBytes(),
					StandardCharsets.UTF_8);

			JsonMapper jsonMapper = JsonMapper.builder()
					.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).build();

			personas = jsonMapper.readValue(jsonString, new TypeReference<List<Persona>>(){});
			personas.stream().forEach(p -> System.out.println(p));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@BeforeEach
	void setUp() {
		personas.stream().forEach(p -> personaRepository.save(p));
	}

	@AfterEach
	void tearDown() {
		personaRepository.deleteAll();
	}

	@Test
	@DisplayName("Verificar el numero de personas registradas")
	void checkNumberOfPeople() {
		// When
		int size = personaRepository.findAll().size();
		//personaRepository.findAll().stream().forEach(p -> System.out.println(p));

		// Then
		assertEquals(personas.size(), size);
	}

	@Test
	@DisplayName("Verificar que las personas registradas sean las correctas")
	void checkPeopleInformation() {

		// When
		List<Persona> personasbd = personaRepository.findAll();

		// Then
		for (int i = 0; i < personas.size(); i++) {
			Persona p = personas.get(i);
			Persona pbd = personasbd.get(i);

			// Validar que los datos sean iguales
			assertThat(p).usingRecursiveComparison().ignoringFields("id").isEqualTo(pbd);
			assertNotNull(p);
			assertNotNull(pbd);
		}
	}

}
