package br.com.felmanc.apitransacao.unit.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.felmanc.apitransacao.dtos.TransacaoDTO;
import br.com.felmanc.apitransacao.exceptions.TransacaoInvalidaException;
import br.com.felmanc.apitransacao.services.TransacoesService;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class TransacoesServiceTest {

    private TransacoesService transacoesService;

    @BeforeEach
    void setUp() {
        transacoesService = new TransacoesService();
    }

    @Test
    @Order(1)
    void deveAdicionarTransacaoValida() {
        TransacaoDTO dto = new TransacaoDTO(100.0, OffsetDateTime.now());
        assertDoesNotThrow(() -> transacoesService.adicionarTransacao(dto));

        assertTrue(transacoesService.filtrarTransacoes(60l).contains(dto));
    }

    @Test
    @Order(2)
    void deveLancarExcecaoParaValorNulo() {
        TransacaoDTO dto = new TransacaoDTO(null, OffsetDateTime.now().minusMinutes(1));
        TransacaoInvalidaException excecao = assertThrows(TransacaoInvalidaException.class, () -> transacoesService.adicionarTransacao(dto));
        assertNull(excecao.getMessage());
    }

    @Test
    @Order(3)
    void deveLancarExcecaoParaDataNula() {
        TransacaoDTO dto = new TransacaoDTO(100.0, null);
        TransacaoInvalidaException excecao = assertThrows(TransacaoInvalidaException.class, () -> transacoesService.adicionarTransacao(dto));
        assertNull(excecao.getMessage());
    }

    @Test
    @Order(4)
    void deveLancarExcecaoParaValorNegativo() {
        TransacaoDTO dto = new TransacaoDTO(-50.0, OffsetDateTime.now().minusMinutes(1));
        TransacaoInvalidaException excecao = assertThrows(TransacaoInvalidaException.class, () -> transacoesService.adicionarTransacao(dto));
        assertNull(excecao.getMessage());
    }

    @Test
    @Order(5)
    void deveLancarExcecaoParaDataFutura() {
        TransacaoDTO dto = new TransacaoDTO(100.0, OffsetDateTime.now().plusMinutes(10));
        TransacaoInvalidaException excecao = assertThrows(TransacaoInvalidaException.class, () -> transacoesService.adicionarTransacao(dto));
        assertNull(excecao.getMessage());
    }

    @Test
    @Order(6)
    void deveLimparTransacoes() {
        TransacaoDTO dto = new TransacaoDTO(100.0, OffsetDateTime.now());
        transacoesService.adicionarTransacao(dto);
        transacoesService.limparTransacoes();
        assertTrue(transacoesService.filtrarTransacoes(60L).isEmpty());
    }

    @Test
    @Order(7)
    void deveFiltrarTransacoesCorretamente() {
        TransacaoDTO transacao1 = new TransacaoDTO(100.0, OffsetDateTime.now().minusSeconds(30));
        TransacaoDTO transacao2 = new TransacaoDTO(50.0, OffsetDateTime.now().minusSeconds(90));
        
        transacoesService.adicionarTransacao(transacao1);
        transacoesService.adicionarTransacao(transacao2);
        
        List<TransacaoDTO> filtradas1 = transacoesService.filtrarTransacoes(60L);
        assertEquals(1, filtradas1.size());
        assertTrue(filtradas1.contains(transacao1));

        List<TransacaoDTO> filtradas2 = transacoesService.filtrarTransacoes(120L);
        assertEquals(2, filtradas2.size());
        assertTrue(filtradas2.contains(transacao2));
    }
}
