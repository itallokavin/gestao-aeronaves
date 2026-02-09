package com.sonda.gestao_aeronaves.web.exception;

import com.sonda.gestao_aeronaves.domain.exception.AircraftNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseException> handleValidation(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());

        return buildResponse(HttpStatus.BAD_REQUEST, "Validation Failed", "Campos obrigatórios ausentes ou incorretos",
                details);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseException> handleReadableError(HttpMessageNotReadableException ex) {
        String message = "Erro na formatação do JSON";
        if (ex.getMessage().contains("Brand")) {
            message = "Marca inválida. Use: EMBRAER, BOEING ou AIRBUS.";
        }

        return buildResponse(HttpStatus.BAD_REQUEST, "Malformed JSON", message, List.of());
    }

    @ExceptionHandler(AircraftNotFoundException.class)
    public ResponseEntity<ResponseException> handleAircraftNotFound(AircraftNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), List.of());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseException> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid Argument", ex.getMessage(), List.of());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseException> handleRuntime(RuntimeException ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(), List.of());
    }

    private ResponseEntity<ResponseException> buildResponse(HttpStatus status, String error, String message,
            List<String> details) {
        ResponseException responseException = new ResponseException(LocalDateTime.now(), status.value(), error, message,
                details);
        return new ResponseEntity<>(responseException, status);
    }
}