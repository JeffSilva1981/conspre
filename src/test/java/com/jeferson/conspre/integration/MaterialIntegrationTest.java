package com.jeferson.conspre.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MaterialIntegrationTest {

    @LocalServerPort
    private int port;

    private Integer materialId;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    // 🟢 CREATE
    @Test
    void deveCriarMaterial() {

        String nomeUnico = "Material " + System.currentTimeMillis();

        String json = """
                {
                   "name": "%s",
                   "unitOfMeasure": "UN",
                   "currentStock": 120,
                   "minimumStock": 30,
                   "ativo": true,
                   "categories": [
                     { "id": 1 }
                   ]
                 }
                """.formatted(nomeUnico);

        materialId =
                RestAssured
                        .given()
                        .contentType(ContentType.JSON)
                        .body(json)
                        .when()
                        .post("/materials")
                        .then()
                        .statusCode(201)
                        .body("id", notNullValue())
                        .body("name", equalTo(nomeUnico))
                        .extract()
                        .path("id");
    }

    // 🔵 GET ALL
    @Test
    void deveListarMateriais() {
        RestAssured
                .given()
                .when()
                .get("/materials")
                .then()
                .statusCode(200);
    }

    // 🔵 GET BY ID
    @Test
    void deveBuscarMaterialPorId() {

        deveCriarMaterial();

        RestAssured
                .given()
                .when()
                .get("/materials/{id}", materialId)
                .then()
                .statusCode(200)
                .body("id", equalTo(materialId));
    }

    // 🟡 UPDATE
    @Test
    void deveAtualizarMaterial() {

        deveCriarMaterial();

        Map<String, Object> jsonAtualizado = Map.of(
                "name", "Cimento Atualizado",
                "unitOfMeasure", "UN",
                "currentStock", new BigDecimal("200"),
                "minimumStock", new BigDecimal("50"),
                "ativo", true,
                "categories", List.of(
                        Map.of("id", 1)
                )
        );

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(jsonAtualizado)
                .when()
                .put("/materials/{id}", materialId)
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo("Cimento Atualizado"));
    }

    // 🔴 DELETE
    @Test
    void deveDeletarMaterial() {

        deveCriarMaterial();

        RestAssured
                .given()
                .when()
                .delete("/materials/{id}", materialId)
                .then()
                .statusCode(204);

        // Agora deve EXISTIR, mas inativo
        RestAssured
                .given()
                .when()
                .get("/materials/{id}", materialId)
                .then()
                .statusCode(200)
                .body("ativo", equalTo(false));
    }
}