package com.alca.config.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alca.model.Persona;
import com.alca.repository.PersonaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PersonaControllerTest {
	private final static String BASE_URI = "http://localhost";
	private final static Integer PORT = 9090;
	
	@Autowired
	private PersonaRepository personaRepository;
	
	private static List<Persona> personasFileStore;
	private List<Persona> personasSaveApiService;

	@BeforeAll
	public static void beforeAll() {
		RestAssured.baseURI = BASE_URI;	
		RestAssured.port = PORT;
		
		try {
			String jsonString = new String(new ClassPathResource("persona-data.json").getInputStream().readAllBytes(),
					StandardCharsets.UTF_8);

			JsonMapper jsonMapper = JsonMapper.builder()
					.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).build();

			personasFileStore = jsonMapper.readValue(jsonString, new TypeReference<List<Persona>>(){});
			personasFileStore.stream().forEach(p -> System.out.println(p));
			
			// Registrar las personas
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@BeforeEach
	void setUp() {
		personasSaveApiService = new ArrayList<Persona>();
		personasFileStore.stream().forEach(p -> {
			personaRepository.save(p);
			personasSaveApiService.add(p);
		});
	}

	@AfterEach
	void tearDown() {
		personaRepository.deleteAll();
		personasSaveApiService.clear();
		personasSaveApiService = null;
	}

	@Test
	@DisplayName("Verificar el servicio que devuelve la lista de personas")
	public void givenUrl_whenSuccessOnGetsAllPeoplesResponse_thenCorrect() {		
		given()
			.contentType(ContentType.JSON)
			.when()
			.get("/personas")
			.then()
			.assertThat()
			.log().all()
			.statusCode(200)
			.body("size()", greaterThan(0));
	}
	
	@Test
	@DisplayName("Verificar el servicio que devuelve el detalle de una persona")
	public void givenUrl_whenSuccessOnGetsDetailPeopleResponse_thenCorrect() {
		Persona persona = personasSaveApiService.get(0);
		given()
			.contentType(ContentType.JSON)
			.when()
			.get("/personas/" + persona.getId())
			.then()
			.assertThat()
			.log().all()
			.statusCode(200)
			.body("id", equalTo(persona.getId()))
			.body("nombre", equalTo(persona.getNombre()))
			.body("apellidos", equalTo(persona.getApellidos()))
			.body("edad", equalTo(persona.getEdad()))
			.body("email", equalTo(persona.getEmail()));
	}
	
	@Test
	@DisplayName("Verificar el servicio que guarda informacion de una persona")
	public void givenUrl_whenSuccessOnPostSavePeopleResponse_thenCorrect() throws JSONException {
		JSONObject newPersona = new JSONObject();
		
		newPersona.put("nombre", "Timmy");
		newPersona.put("apellidos", "Jackson");
		newPersona.put("edad", "36");
		newPersona.put("email", "tjackson@gmail.com");
		
		given()
			.contentType(ContentType.JSON).body(newPersona.toString())
			.when()
			.post("/personas")
			.then()
			.log().all()
			.assertThat()
			.statusCode(201)
			.body("id", notNullValue())
			.body("nombre", equalTo("Timmy"))
			.body("apellidos", equalTo("Jackson"))
			.body("edad", equalTo(36))
			.body("email", equalTo("tjackson@gmail.com"));
	}
	
	 @Test
	 @DisplayName("Verificar el servicio que actualiza informacion de una persona")
	 public void givenUrl_whenSuccessOnUpdateResponse_thenCorrect() throws JSONException, JsonProcessingException {
		 JsonMapper jsonMapper = JsonMapper.builder()
					.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).build();
		 
		 Persona persona = personasSaveApiService.get(0);
		 
		 Persona updatedPersona = Persona.builder()
				 .nombre("Benito")
				 .apellidos("Siancas")
				 .edad(53)
				 .email("bsiancas@gmail.com")
				 .build();		 
		 
		 String jsonBody = jsonMapper.writeValueAsString(updatedPersona);
			
		 given()
			.contentType(ContentType.JSON).body(jsonBody)
			.when()
			.put("/personas/" + persona.getId())
			.then()
			.log().all()
			.assertThat()
			.statusCode(201)
			.body("id", equalTo(persona.getId()))
			.body("nombre", equalTo(updatedPersona.getNombre()))
			.body("apellidos", equalTo(updatedPersona.getApellidos()))
			.body("edad", equalTo(updatedPersona.getEdad()))
			.body("email", equalTo(updatedPersona.getEmail()));
	 }
	 
	 @Test
	 @DisplayName("Verificar el servicio que borra la informacion de una persona")
	 public void givenUrl_whenSuccessOnDeleteResponse_thenCorrect() {
		 Persona persona = personasSaveApiService.get(0);
		 
		 // Eliminar los datos de la persona
		 given()
		 	.contentType(ContentType.JSON)
		 	.when()
		 	.delete("/personas/" + persona.getId())
		 	.then()
		 	.log().all()
		 	.assertThat().statusCode(204);
		 
		 // Verificar que la persona ya no exista
		 given()
			.contentType(ContentType.JSON)
			.when()
			.get("/personas/" + persona.getId())
			.then()
			.assertThat()
			.log().all()
			.statusCode(404);
	 }
}
