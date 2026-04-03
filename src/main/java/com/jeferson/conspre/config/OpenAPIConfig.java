package com.jeferson.conspre.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI stockControlAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Conspre")
                        .description("Inventory control and material requisition system")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Jeferson")
                                .email("jeff.contactwork@gmail.com"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Complete Documentation")
                        .url("https://github.com/JeffSilva1981/conspre"));
    }
}
