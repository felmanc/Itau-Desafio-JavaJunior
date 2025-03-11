package br.com.felmanc.apitransacao.unit.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.felmanc.apitransacao.controllers.TransacaoController;
import br.com.felmanc.apitransacao.dtos.TransacaoDTO;
import br.com.felmanc.apitransacao.services.TransacoesService;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class TransacaoControllerTest {

    @Mock
    private TransacoesService transacoesService;

    @InjectMocks
    private TransacaoController transacaoController;

    @Test
    @Order(1)
    void deveRetornarStatus201AoAdicionarTransacao() {
        // Arrange
        TransacaoDTO dto = new TransacaoDTO(100.0, OffsetDateTime.now());

        // Act
        ResponseEntity<Void> resposta = transacaoController.adicionarTransacao(dto);

        // Assert
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        verify(transacoesService).adicionarTransacao(dto); // Verifica que o serviço foi chamado
    }

    @Test
    @Order(2)
    void deveRetornarStatus200AoLimparTransacoes() {
        // Act
        ResponseEntity<Void> resposta = transacaoController.limparTransacoes();

        // Assert
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        verify(transacoesService).limparTransacoes(); // Verifica que o serviço foi chamado
    }
}
