package br.com.felmanc.apitransacao.exceptions;

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
                .body("");
    }
        
    @ExceptionHandler(TransacaoInvalidaException.class)
    public ResponseEntity<Object> handleTransacaoInvalida(TransacaoInvalidaException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body("");
    }    

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleGenericException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("");
    }
}
