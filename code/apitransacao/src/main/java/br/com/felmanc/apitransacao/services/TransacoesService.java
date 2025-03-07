package br.com.felmanc.apitransacao.services;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.felmanc.apitransacao.dtos.TransacaoDTO;
import br.com.felmanc.apitransacao.excecoes.TransacaoInvalidaException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
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
}

