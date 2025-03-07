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


@RestController
@RequiredArgsConstructor
@RequestMapping("/transacao")
@Tag(name = "Transacao", description = "Endpoints para gerenciar transações")
@ApiResponses(value = {
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
})
public class TransacaoController {

	private final TransacoesService transacoesService;
	
    @Operation(summary = "Adicionar uma nova transação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transação criada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Erro de validação, a transação não foi aceita", content = {}),
            @ApiResponse(responseCode = "400", description = "Erro na requisição (JSON inválido ou erro de sintaxe)", content = {})
    })
    @PostMapping
	public ResponseEntity<Void> AdicionarTransacao(@RequestBody TransacaoDTO dto) {
		
		transacoesService.AdicionarTransacao(dto);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
    
    @DeleteMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todas as informações foram apagadas com sucesso")
    })    
    public ResponseEntity<Void> LimparTransacoes() {
    	
    	transacoesService.LimparTransacoes();
    	
    	return ResponseEntity.status(HttpStatus.OK).build();
    }
}
