package br.com.felmanc.apitransacao.dtos;

import java.time.OffsetDateTime;

public record TransacaoDTO(Double valor, OffsetDateTime dataHora) {

}
