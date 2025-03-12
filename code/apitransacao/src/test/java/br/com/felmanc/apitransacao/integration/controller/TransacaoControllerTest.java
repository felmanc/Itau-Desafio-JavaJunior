package br.com.felmanc.apitransacao.integration.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

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
public class TransacaoControllerTest {
    
    @Test
	@Order(1)
    @DirtiesContext
    public void testAdicionarTransacaoSucesso() {
        log.info("Executando testAdicionarTransacaoSucesso");
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
            .statusCode(201)
            .body(Matchers.blankOrNullString());
        
        log.info("Finalizado testAdicionarTransacaoSucesso");
    }

    @Test
	@Order(2)
    public void testAdicionarTransacaoValorNegativo() {
        log.info("Executando testAdicionarTransacaoValorNegativo");
        String invalidJson = "{\"valor\": -123.45, \"dataHora\": \"" + OffsetDateTime.now().minusMinutes(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + "\"}";

        given()
            .log().all()
	        .port(TestConfigs.SERVER_PORT)
            .contentType(ContentType.JSON)
            .body(invalidJson)
        .when()
            .post("/transacao")
        .then()
            .log().all()
            .statusCode(422)
            .body("error", equalTo("UNPROCESSABLE_ENTITY"))
            .body("message", equalTo("A transação enviada é inválida. Consulte os critérios necessários."))
            .body("details", equalTo("O valor da transação não pode ser negativo."));

		log.info("Finalizado testAdicionarTransacaoValorNegativo");
    }

    @Test
	@Order(3)
    public void testAdicionarTransacaoValorInexistente() {
        log.info("Executando testAdicionarTransacaoValorNegativo");
        String invalidJson = "{\"dataHora\": \"" + OffsetDateTime.now().minusMinutes(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + "\"}";

        given()
            .log().all()
	        .port(TestConfigs.SERVER_PORT)
            .contentType(ContentType.JSON)
            .body(invalidJson)
        .when()
            .post("/transacao")
        .then()
            .log().all()
            .statusCode(422)
            .body("error", equalTo("UNPROCESSABLE_ENTITY"))
            .body("message", equalTo("A transação enviada é inválida. Consulte os critérios necessários."))
            .body("details", equalTo("O valor da transação não pode ser nulo."));

		log.info("Finalizado testAdicionarTransacaoValorInexistente");
    }
    
    @Test
	@Order(4)
    public void testAdicionarTransacaoDataFutura() {
        String invalidJson = "{\"valor\": 123.45, \"dataHora\": \"" + OffsetDateTime.now().plusMinutes(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + "\"}";

        given()
            .log().all()
	        .port(TestConfigs.SERVER_PORT)
            .contentType(ContentType.JSON)
            .body(invalidJson)
        .when()
            .post("/transacao")
        .then()
            .log().all()
            .statusCode(422)
            .body("error", equalTo("UNPROCESSABLE_ENTITY"))
            .body("message", equalTo("A transação enviada é inválida. Consulte os critérios necessários."))
            .body("details", equalTo("A data da transação deve estar no passado."));

		log.info("Finalizado testAdicionarTransacaoDataFutura");
    }

    @Test
	@Order(5)
    public void testAdicionarTransacaoDataInexistente() {
        String invalidJson = "{\"valor\": 123.45}";

        given()
            .log().all()
	        .port(TestConfigs.SERVER_PORT)
            .contentType(ContentType.JSON)
            .body(invalidJson)
        .when()
            .post("/transacao")
        .then()
            .log().all()
            .statusCode(422)
        .body("error", equalTo("UNPROCESSABLE_ENTITY"))
        .body("message", equalTo("A transação enviada é inválida. Consulte os critérios necessários."))
        .body("details", equalTo("A data da transação não pode ser nula."));

		log.info("Finalizado testAdicionarTransacaoDataInexistente");
    }    
    
    @Test
	@Order(6)
    public void testAdicionarTransacaoJsonInvalido() {
        log.info("Executando testAdicionarTransacaoJsonInvalido");
        String invalidJson = "{\"valor\": 123.45, , \"dataHora\": \"" + OffsetDateTime.now().minusMinutes(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + "\"}";

        given()
            .log().all()
	        .port(TestConfigs.SERVER_PORT)
            .contentType(ContentType.JSON)
            .body(invalidJson)
        .when()
            .post("/transacao")
        .then()
            .log().all()
            .statusCode(400)
            .body("error", equalTo("BAD_REQUEST"))
            .body("message", equalTo("O JSON enviado não pôde ser lido. Verifique a formatação da requisição."))
            .body("details", containsString("Unexpected character (',' (code 44))"));

		log.info("Finalizado testAdicionarTransacaoJsonInvalido");
    }    

    @Test
	@Order(7)
    public void testAdicionarTransacaoEndpointInvalido() {
        log.info("Executando testAdicionarTransacaoEndpointInvalido");
        String validJson = "{\"valor\": 123.45, \"dataHora\": \"" + OffsetDateTime.now().minusMinutes(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + "\"}";

        given()
        .log().all()        
        .port(TestConfigs.SERVER_PORT)
            .contentType(ContentType.JSON)
            .body(validJson)     
        .when()
            .post("/transac") // Endpoint inexistente
        .then()
            .log().all()
            .statusCode(404);

		log.info("Finalizado testAdicionarTransacaoEndpointInvalido");
    }       
    
    @Test
	@Order(8)
    public void testAdicionarTransacaoError() {
        log.info("Executando testAdicionarTransacaoError");
        String invalidJson = "{\"valor\": 123.45, \"dataHora\": \"2020-08-07T12:34:56\"}"; // Faltando o Offset

        given()
            .log().all()
	        .port(TestConfigs.SERVER_PORT)
            .contentType(ContentType.JSON)
            .body(invalidJson)
        .when()
            .post("/transacao")
        .then()
            .log().all()
            .statusCode(400)
            .body("error", equalTo("BAD_REQUEST"))
            .body("message", equalTo("O JSON enviado não pôde ser lido. Verifique a formatação da requisição."))
            .body("details", containsString("Cannot deserialize value of type `java.time.OffsetDateTime`"));

		log.info("Finalizado testAdicionarTransacaoError");
    }

	@Test
	@Order(9)
	public void testLimparTransacoes() {
        log.info("Executando testLimparTransacoes");

		given()
            .log().all()
	        .port(TestConfigs.SERVER_PORT)
	        .when()
	        .delete("/transacao")
	        .then()
            .log().all()
            .statusCode(200)
            .body(Matchers.blankOrNullString());

		log.info("Finalizado testLimparTransacoes");
	}
}