package br.com.felmanc.apitransacao;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransacaoControllerTest {

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080"; // URL da API
        log.info("Configuração inicial dos testes concluída.");
    }

	@Test
	@Order(1)
	public void testCalcularEstatisticasSemTransacoes() {
        log.info("Executando testCalcularEstatisticasSemTransacoes");
		given()
            .log().all()
        .when()
        .delete("/transacao")
        .then()
            .log().all()
        .statusCode(200); // Espera status 200 OK	
		
		given()
            .log().all()
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
    @DirtiesContext
    public void testAdicionarTransacaoSucesso() {
        log.info("Executando testAdicionarTransacaoSucesso");
        String validJson = "{\"valor\": 123.45, \"dataHora\": \"" + OffsetDateTime.now().minusMinutes(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + "\"}";

        given()
            .log().all()
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
	@Order(3)
    public void testAdicionarTransacaoValorNegativo() {
        log.info("Executando testAdicionarTransacaoValorNegativo");
        String invalidJson = "{\"valor\": -123.45, \"dataHora\": \"" + OffsetDateTime.now().minusMinutes(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + "\"}";

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(invalidJson)
        .when()
            .post("/transacao")
        .then()
            .log().all()
            .statusCode(422)
            .body(Matchers.blankOrNullString());

		log.info("Finalizado testAdicionarTransacaoValorNegativo");
    }

    @Test
	@Order(4)
    public void testAdicionarTransacaoValorInexistente() {
        log.info("Executando testAdicionarTransacaoValorNegativo");
        String invalidJson = "{\"dataHora\": \"" + OffsetDateTime.now().minusMinutes(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + "\"}";

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(invalidJson)
        .when()
            .post("/transacao")
        .then()
            .log().all()
            .statusCode(422)
            .body(Matchers.blankOrNullString());

		log.info("Finalizado testAdicionarTransacaoValorInexistente");
    }
    
    @Test
	@Order(5)
    public void testAdicionarTransacaoDataFutura() {
        String invalidJson = "{\"valor\": 123.45, \"dataHora\": \"" + OffsetDateTime.now().plusMinutes(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + "\"}";

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(invalidJson)
        .when()
            .post("/transacao")
        .then()
            .log().all()
            .statusCode(422)
            .body(Matchers.blankOrNullString());

		log.info("Finalizado testAdicionarTransacaoDataFutura");
    }

    @Test
	@Order(6)
    public void testAdicionarTransacaoDataInexistente() {
        String invalidJson = "{\"valor\": 123.45}";

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(invalidJson)
        .when()
            .post("/transacao")
        .then()
            .log().all()
            .statusCode(422)
            .body(Matchers.blankOrNullString());

		log.info("Finalizado testAdicionarTransacaoDataInexistente");
    }    
    
    @Test
	@Order(7)
    public void testAdicionarTransacaoJsonInvalido() {
        log.info("Executando testAdicionarTransacaoJsonInvalido");
        String invalidJson = "{\"valor\": 123.45, , \"dataHora\": \"" + OffsetDateTime.now().minusMinutes(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + "\"}";

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(invalidJson)
        .when()
            .post("/transacao")
        .then()
            .log().all()
            .statusCode(400)
            .body(Matchers.blankOrNullString());

		log.info("Finalizado testAdicionarTransacaoJsonInvalido");
    }    

    @Test
	@Order(8)
    public void testAdicionarTransacaoEndpointInvalido() {
        log.info("Executando testAdicionarTransacaoEndpointInvalido");
        String validJson = "{\"valor\": 123.45, \"dataHora\": \"" + OffsetDateTime.now().minusMinutes(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + "\"}";

        given()
        .log().all()        
            .contentType(ContentType.JSON)
            .body(validJson)     
        .when()
            .post("/transac")
        .then()
            .log().all()
            .statusCode(404);

		log.info("Finalizado testAdicionarTransacaoEndpointInvalido");
    }       
    
    @Test
	@Order(9)
    public void testAdicionarTransacaoError() {
        log.info("Executando testAdicionarTransacaoError");
        String invalidJson = "{\"valor\": 123.45, \"dataHora\": \"2020-08-07T12:34:56\"}"; // Faltando o Offset

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(invalidJson)
        .when()
            .post("/transacao")
        .then()
            .log().all()
            .statusCode(400)
            .body(Matchers.blankOrNullString());

		log.info("Finalizado testAdicionarTransacaoError");
    }

	@Test
	@Order(10)
	public void testLimparTransacoes() {
        log.info("Executando testLimparTransacoes");
        String validJson = "{\"valor\": 123.45, \"dataHora\": \"" + OffsetDateTime.now().minusMinutes(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + "\"}";

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(validJson)
        .when()
            .post("/transacao")
        .then()
            .log().all()
            .statusCode(201);
		
		given()
            .log().all()
	        .when()
	        .delete("/transacao")
	        .then()
            .log().all()
            .statusCode(200)
            .body(Matchers.blankOrNullString());
	    
	    given()
            .log().all()
        .when()
        .get("/estatistica")
        .then()
            .log().all()
        .statusCode(200)
        .body("count", equalTo(0)) // Espera 0 transações
        .body("sum", equalTo(0.0f)) // Espera soma 0.0
        .body("avg", equalTo(0.0f)) // Espera média 0.0
        .body("min", equalTo(0.0f)) // Espera valor mínimo 0.0
        .body("max", equalTo(0.0f)); // Espera valor máximo 0.0	    

		log.info("Finalizado testLimparTransacoes");
	}

	@Test
	@Order(11)
    @DirtiesContext	
	public void testCalcularEstatisticasComTransacoes() {
        log.info("Executando testCalcularEstatisticasComTransacoes");

        // Garante que não haja transações
		given()
            .log().all()
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