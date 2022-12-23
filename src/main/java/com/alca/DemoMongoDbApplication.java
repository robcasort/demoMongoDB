package com.alca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(info=@Info(title="API de Personas", 
	description = "API para gestionar las personas",
	version = "0.0.1",		
	termsOfService = "url terminos y condiciones del servicio",
	license = @License(name = "Licencia", url="url de la licencia"),
	contact = @Contact(name = "Tecnologia", url = "www.company.com", email = "usuario@company.com")
	))
public class DemoMongoDbApplication {

	/**
	 *  The OpenAPI descriptions will be available at the path /v3/api-docs
	 *  http://localhost:8080/v3/api-docs/
	 * 
	 *  To use a custom path, we can indicate in the application.properties file:
	 *  springdoc.api-docs.path=/api-docs
	 *  Access:
	 *  http://localhost:8080/api-docs/
	 *  http://localhost:8080/api-docs.yaml
	 *  
	 *  Integration with Swagger UI
	 *  http://localhost:8080/swagger-ui.html
	 *  
	 *  customize the path of our API documentation. We can do this by modifying our application.properties to include:
	 *  springdoc.swagger-ui.path=/swagger-ui-custom.html
	 *  So now our API documentation will be available at http://localhost:8080/swagger-ui-custom.html.
	 */
	
	public static void main(String[] args) {
		SpringApplication.run(DemoMongoDbApplication.class, args);
	}
}
