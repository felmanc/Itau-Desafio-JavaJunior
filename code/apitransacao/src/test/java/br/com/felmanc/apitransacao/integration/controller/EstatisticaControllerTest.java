package br.com.felmanc.apitransacao.integration.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import br.com.felmanc.configs.TestConfigs;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class EstatisticaControllerTest {

	@Test
	@Order(1)
	public void testCalcularEstatisticasSemTransacoes() {
        log.info("Executando testCalcularEstatisticasSemTransacoes");
		given()
            .log().all()
	        .port(TestConfigs.SERVER_PORT)
        .when()
        .delete("/transacao")
        .then()
            .log().all()
        .statusCode(200); // Espera status 200 OK	
		
		given()
            .log().all()
	        .port(TestConfigs.SERVER_PORT)
	        .when()
	        .get("/estatistica")
	        .then()
            .log().all()
	        .statusCode(200)
	        .body("count", equalTo(0))
	        .body("sum", equalTo(0.0f))
	        .body("avg", equalTo(0.0f))
	        .body("min", equalTo(0.0f))
	        .body("max", equalTo(0.0f));

		log.info("Finalizado testCalcularEstatisticasSemTransacoes");
	}

	@Test
	@Order(2)
	public void testLimparTransacoes() {
        log.info("Executando testLimparTransacoes");
        String validJson = "{\"valor\": 123.45, \"dataHora\": \"" + OffsetDateTime.now().minusMinutes(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + "\"}";

        given()
            .log().all()
	        .port(TestConfigs.SERVER_PORT)
            .contentType(ContentType.JSON)
            .body(validJson)
        .when()
            .post("/transacao")
        .then()
            .log().all()
            .statusCode(201);
		
		given()
            .log().all()
	        .port(TestConfigs.SERVER_PORT)
	        .when()
	        .delete("/transacao")
	        .then()
            .log().all()
            .statusCode(200)
            .body(Matchers.blankOrNullString());
	    
	    given()
            .log().all()
	        .port(TestConfigs.SERVER_PORT)
        .when()
        .get("/estatistica")
        .then()
            .log().all()
        .statusCode(200)
        .body("count", equalTo(0))
        .body("sum", equalTo(0.0f))
        .body("avg", equalTo(0.0f))
        .body("min", equalTo(0.0f))
        .body("max", equalTo(0.0f));	    

		log.info("Finalizado testLimparTransacoes");
	}

	@Test
	@Order(3)
    @DirtiesContext	
	public void testCalcularEstatisticasComTransacoes() {
        log.info("Executando testCalcularEstatisticasComTransacoes");

        // Garante que não haja transações
		given()
            .log().all()
	        .port(TestConfigs.SERVER_PORT)
        .when()
        .delete("/transacao")
        .then()
            .log().all()
            .statusCode(200);	
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

		String validJson1 = "{\"valor\": 100.00, \"dataHora\": \"" 
		    + OffsetDateTime.now().format(formatter) + "\"}";

	    // Envia uma transação válida
        given()
	        .log().all()
	        .port(TestConfigs.SERVER_PORT)
	        .contentType(ContentType.JSON)
	        .body(validJson1)
	    .when()
	        .post("/transacao")
	    .then()
	        .log().all()
	        .statusCode(201);


		String validJson2 = "{\"valor\": 200.00, \"dataHora\": \"" 
			    + OffsetDateTime.now().minusSeconds(61).format(formatter) + "\"}";
        
	    // Envia uma transação válida
        given()
	        .log().all()
	        .port(TestConfigs.SERVER_PORT)
	        .contentType(ContentType.JSON)
	        .body(validJson2)
	    .when()
	        .post("/transacao")
	    .then()
	        .log().all()
	        .statusCode(201);

        // Valida estatistica com 1 transação
	    given()
	        .log().all()
	        .port(TestConfigs.SERVER_PORT)
	        .contentType(ContentType.JSON)
	    .when()
	        .get("/estatistica")
	    .then()
        	.log().all()
	        .statusCode(200)
	        .body("count", equalTo(1))
	        .body("sum", equalTo(100.00f))
	        .body("avg", equalTo(100.00f))
	        .body("min", equalTo(100.00f))
	        .body("max", equalTo(100.00f));

        // Valida estatistica com 2 transações
	    given()
	        .log().all()
	        .port(TestConfigs.SERVER_PORT)
	        .contentType(ContentType.JSON)
        .when()
	        .get("/estatistica?intervalo=120")
        .then()
        	.log().all()	        
	        .statusCode(200)
	        .body("count", equalTo(2))
	        .body("sum", equalTo(300.00f))
	        .body("avg", equalTo(150.00f))
	        .body("min", equalTo(100.00f))
	        .body("max", equalTo(200.00f));

		log.info("Finalizado testCalcularEstatisticasComTransacoes");
	}	
}