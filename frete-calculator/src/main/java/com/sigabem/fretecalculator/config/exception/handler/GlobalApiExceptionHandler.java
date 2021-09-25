package com.sigabem.fretecalculator.config.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.*;

@ControllerAdvice
@Slf4j
public class GlobalApiExceptionHandler {

    @ExceptionHandler(InvalidCpfException.class)
    public ResponseEntity<?> InvalidCpfExceptionHandler(HttpServletRequest req, InvalidCpfException ex) {
        log.error("InvalidCpfException{}\n", req.getRequestURI(), ex);
        return new ResponseEntity<>(new ApiExceptionDetails<>(new Date().getTime(),
                "CEP inválido", List.of(ex.getMsg())), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiExceptionDetails<Map<String, String>>> methodArgumentNotValidException(
            HttpServletRequest req, MethodArgumentNotValidException ex) {

        List<Map<String, String>> details = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            Map<String, String> detail = new HashMap<>();
            detail.put("Objeto", fieldError.getObjectName());
            detail.put("Campo", fieldError.getField());
            detail.put("Valor recusado", String.valueOf(fieldError.getRejectedValue()));
            detail.put("Messagem", fieldError.getDefaultMessage());
            details.add(detail);
        });
        log.error("methodArgumentNotValidException{}\n", req.getRequestURI(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiExceptionDetails<Map<String, String>>(new Date().getTime(), "Campo(s) inválidos", details));
    }

}
