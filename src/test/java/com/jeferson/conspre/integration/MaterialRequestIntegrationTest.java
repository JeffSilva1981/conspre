package com.jeferson.conspre.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MaterialRequestIntegrationTest {

    @LocalServerPort
    private int port;

    private Integer requestId;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    // 🟢 TESTE: Criar requisição
    @Test
    void deveCriarMaterialRequest() {

        String json = """
                {
                  "employeeId": 1,
                  "userId": 1,
                  "observation": "Teste integração",
                  "items": [
                    {
                      "materialId": 1,
                      "quantity": 2
                    }
                  ]
                }
                """;

        requestId =
                RestAssured
                        .given()
                        .contentType(ContentType.JSON)
                        .body(json)
                        .when()
                        .post("/material-requests")
                        .then()
                        .statusCode(201)
                        .body("id", notNullValue())
                        .body("observation", equalTo("Teste integração"))
                        .body("items", hasSize(1))
                        .extract()
                        .path("id");
    }

    // 🔵 TESTE: Buscar por ID
    @Test
    void deveBuscarMaterialRequestPorId() {

        deveCriarMaterialRequest();

        RestAssured
                .given()
                .when()
                .get("/material-requests/{id}", requestId)
                .then()
                .statusCode(200)
                .body("id", equalTo(requestId));
    }

    // 🔵 TESTE: Listar
    @Test
    void deveListarMaterialRequests() {

        RestAssured
                .given()
                .when()
                .get("/material-requests")
                .then()
                .statusCode(200)
                .body("content", notNullValue());
    }

    @Test
    void deveDeletarMaterialRequest() {

        deveCriarMaterialRequest();

        RestAssured
                .given()
                .when()
                .delete("/material-requests/{id}", requestId)
                .then()
                .statusCode(204);

        // Agora correto: não deve encontrar mais
        RestAssured
                .given()
                .when()
                .get("/material-requests/{id}", requestId)
                .then()
                .statusCode(404);
    }
}