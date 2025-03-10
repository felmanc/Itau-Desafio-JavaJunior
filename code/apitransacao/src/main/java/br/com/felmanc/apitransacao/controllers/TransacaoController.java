package br.com.felmanc.apitransacao.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.felmanc.apitransacao.dtos.TransacaoDTO;
import br.com.felmanc.apitransacao.services.TransacoesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transacao")
@Tag(name = "Transacao", description = "Endpoints para gerenciar transações")
@Slf4j
public class TransacaoController {

    private final TransacoesService transacoesService;

    @Operation(summary = "Adicionar uma nova transação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transação criada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Erro de validação, a transação não foi aceita", content = {}),
            @ApiResponse(responseCode = "400", description = "Erro na requisição (JSON inválido ou erro de sintaxe)", content = {})
    })
    @PostMapping
    public ResponseEntity<Void> adicionarTransacao(@RequestBody TransacaoDTO dto) {
        log.info("Requisição para adicionar transação: {}", dto);

        transacoesService.adicionarTransacao(dto);
        log.info("Transação criada com sucesso: {}", dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Limpar transações existentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todas as informações foram apagadas com sucesso")
    })
    @DeleteMapping
    public ResponseEntity<Void> limparTransacoes() {
        log.info("Requisição para limpar transações");

        transacoesService.limparTransacoes();
        log.info("Todas as transações foram apagadas com sucesso.");
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
