package br.com.felmanc.apitransacao.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.felmanc.apitransacao.dtos.EstatisticaDTO;
import br.com.felmanc.apitransacao.dtos.TransacaoDTO;
import br.com.felmanc.apitransacao.services.EstatisticaService;
import br.com.felmanc.apitransacao.services.TransacoesService;

@ExtendWith(MockitoExtension.class)
class EstatisticaServiceTest {

    @Mock
    private TransacoesService mockTransacoesService;

    @InjectMocks
    private EstatisticaService estatisticaService;

    @BeforeEach
    void setUp() {
        estatisticaService = new EstatisticaService(mockTransacoesService);
    }

    @Test
    @Order(1)
    void deveRetornarEstatisticaVaziaQuandoNaoHaTransacoes() {
        when(mockTransacoesService.filtrarTransacoes(60L)).thenReturn(List.of());

        EstatisticaDTO estatistica = estatisticaService.calcularEstatistica(60L);

        assertEquals(0, estatistica.count());
        assertEquals(0.0, estatistica.sum());
        assertEquals(0.0, estatistica.avg());
        assertEquals(0.0, estatistica.min());
        assertEquals(0.0, estatistica.max());
    }

    @Test
    @Order(2)
    void deveCalcularEstatisticaCorretamente() {
        List<TransacaoDTO> transacoes = List.of(
            new TransacaoDTO(100.0, OffsetDateTime.now().minusSeconds(30)),
            new TransacaoDTO(50.0, OffsetDateTime.now().minusSeconds(20)),
            new TransacaoDTO(200.0, OffsetDateTime.now().minusSeconds(10))
        );
        
        when(mockTransacoesService.filtrarTransacoes(60L)).thenReturn(transacoes);

        EstatisticaDTO estatistica = estatisticaService.calcularEstatistica(60L);

        assertEquals(3, estatistica.count());
        assertEquals(350.0, estatistica.sum());
        assertEquals(116.67, estatistica.avg(), 0.01);
        assertEquals(50.0, estatistica.min());
        assertEquals(200.0, estatistica.max());
    }

    @Test
    @Order(3)
    void deveCalcularEstatisticaDentroDoTempoLimite() {
        List<TransacaoDTO> transacoes = List.of(
            new TransacaoDTO(100.0, OffsetDateTime.now().minusSeconds(30)),
            new TransacaoDTO(50.0, OffsetDateTime.now().minusSeconds(20)),
            new TransacaoDTO(200.0, OffsetDateTime.now().minusSeconds(10))
        );

        when(mockTransacoesService.filtrarTransacoes(60L)).thenReturn(transacoes);

        assertTimeout(Duration.ofMillis(20), () -> {
            estatisticaService.calcularEstatistica(60L);
        });
    }
}
