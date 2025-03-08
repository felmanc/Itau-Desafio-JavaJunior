package br.com.felmanc.apitransacao.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@ControllerAdvice
@Tag(name = "Erros Globais", description = "Documentação de erros comuns da API")
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleJsonParseException(HttpMessageNotReadableException ex) {
        return "Erro: O JSON fornecido está mal formatado.";
    }
    
    @ExceptionHandler(TransacaoInvalidaException.class)
    public ResponseEntity<String> handleTransacaoInvalida(TransacaoInvalidaException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                             .body(ex.getMessage());
    }    
	
    @ExceptionHandler(Exception.class)
    @ApiResponses({
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception e) {
        return "Erro interno no servidor";
    }
}
