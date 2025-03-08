package br.com.felmanc.apitransacao.services;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.felmanc.apitransacao.dtos.TransacaoDTO;
import br.com.felmanc.apitransacao.exceptions.TransacaoInvalidaException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransacoesService {
    
    private final List<TransacaoDTO> transacoes = new ArrayList<>();

    public void AdicionarTransacao(TransacaoDTO dto) {
        log.info("Adicionar transação: " + dto);

        if (dto.valor() == null) {
            log.info("Não permitida transação com valor nulo.");
            throw new TransacaoInvalidaException("O valor da transação não pode ser nulo.");
        }         
        
        if (dto.dataHora() == null) {
            log.info("Não permitida transação com data nula.");
            throw new TransacaoInvalidaException("A data da transação não pode ser nula.");
        }

        if (dto.valor() < 0) {
            log.info("Não permitida transação com valor negativo.");
            throw new TransacaoInvalidaException("O valor da transação não pode ser negativo.");
        }

        if (dto.dataHora().isAfter(OffsetDateTime.now())) {
            log.info("Não permitida transação com data futura.");
            throw new TransacaoInvalidaException("A data da transação deve estar no passado.");
        }

        transacoes.add(dto);
        log.info("Transação adicionada com sucesso.");
    }
    
    public void LimparTransacoes() {
        if (transacoes.isEmpty()) {
            log.info("Nenhuma transação para limpar.");
            return;
        }

        log.info("Limpando {} transações existentes", transacoes.size());
        
        transacoes.clear();

        log.info("Limpeza de transações executada com sucesso");
    }

    public List<TransacaoDTO> filtrarTransacoes(Long intervaloSegundos) {
        log.info("Filtrando transações para os últimos {} segundos", intervaloSegundos);
        OffsetDateTime limite = OffsetDateTime.now().minusSeconds(intervaloSegundos);

        List<TransacaoDTO> transacoesFiltradas = transacoes.stream()
                .filter(transacao -> transacao.dataHora().isAfter(limite))
                .toList();

        log.info("{} transações filtradas dentro do intervalo", transacoesFiltradas.size());
        return transacoesFiltradas;
    }

}

