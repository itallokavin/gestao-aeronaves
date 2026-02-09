package com.sonda.gestao_aeronaves.web.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ResponseException {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private List<String> details;
}