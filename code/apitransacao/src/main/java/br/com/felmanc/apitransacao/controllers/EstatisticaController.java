package br.com.felmanc.apitransacao.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.felmanc.apitransacao.dtos.EstatisticaDTO;
import br.com.felmanc.apitransacao.services.EstatisticaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/estatistica")
@Tag(name = "Estatistica", description = "Endpoints para retornar estatísticas das transações")
public class EstatisticaController {

	private final EstatisticaService estatisticaService;
	
    @Operation(summary = "Exibir estatísticas das transações")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estatísticas calculadas com sucesso")
    })    
    @GetMapping
    public ResponseEntity<EstatisticaDTO> calcularEstatistica(
            @RequestParam(name = "intervalo", required =  false, defaultValue = "60") Long intervaloSegundos) {

        EstatisticaDTO estatisticas = estatisticaService.calcularEstatistica(intervaloSegundos);

        return ResponseEntity.ok(estatisticas);
    }
}

