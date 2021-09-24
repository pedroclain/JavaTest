package com.sigabem.fretecalculator.config.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalApiExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<?> HttpClientErrorExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("CEP nao encontrado");
        return new ResponseEntity<>(new ApiExceptionDetails<>(new Date().getTime(),
                "CEP inválido ou não existente", List.of(ex.)), HttpStatus.BAD_REQUEST);

    }
}
