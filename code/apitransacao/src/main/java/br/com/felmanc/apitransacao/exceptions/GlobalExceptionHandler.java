package br.com.felmanc.apitransacao.exceptions;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestControllerAdvice
@Tag(name = "Erros Globais", description = "Documentação de erros comuns da API")
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleJsonParseException(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "BAD_REQUEST",
                        "message", "O JSON enviado não pôde ser lido. Verifique a formatação da requisição.",
                        "details", ex.getMessage()
                ));
    }
        
    @ExceptionHandler(TransacaoInvalidaException.class)
    public ResponseEntity<Object> handleTransacaoInvalida(TransacaoInvalidaException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Map.of(
                        "error", "UNPROCESSABLE_ENTITY",
                        "message", "A transação enviada é inválida. Consulte os critérios necessários.",
                        "details", ex.getMessage()
                ));
    }    

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleGenericException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "error", "INTERNAL_SERVER_ERROR",
                        "message", "Ocorreu um erro inesperado. Por favor, tente novamente mais tarde.",
                        "details", e.getMessage()
                ));
    }
}

