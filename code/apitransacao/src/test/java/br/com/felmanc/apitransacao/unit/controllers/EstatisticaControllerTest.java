package br.com.felmanc.apitransacao.unit.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import br.com.felmanc.apitransacao.controllers.EstatisticaController;
import br.com.felmanc.apitransacao.dtos.EstatisticaDTO;
import br.com.felmanc.apitransacao.services.EstatisticaService;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class EstatisticaControllerTest {

    @Mock
    private EstatisticaService estatisticaService;

    @InjectMocks
    private EstatisticaController estatisticaController;

    @Test
    @Order(1)
    void deveRetornarEstatisticasComStatus200() {
        // Arrange
        EstatisticaDTO estatisticaMock = new EstatisticaDTO(10L, 100.0, 10.0, 5.0, 20.0);
        when(estatisticaService.calcularEstatistica(60L)).thenReturn(estatisticaMock);

        // Act
        ResponseEntity<EstatisticaDTO> resposta = estatisticaController.calcularEstatistica(60L);

        // Assert
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(estatisticaMock, resposta.getBody());
        verify(estatisticaService).calcularEstatistica(60L); // Verifica chamada ao serviço
    }
}
