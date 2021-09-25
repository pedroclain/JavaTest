package com.sigabem.fretecalculator.config.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@ControllerAdvice
@Slf4j
public class GlobalApiExceptionHandler {

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<?> InvalidCpfExceptionHandler(HttpServletRequest req, InvalidParameterException ex) {
        log.error("InvalidParameterException{}\n", req.getRequestURI(), ex);
        return new ResponseEntity<>(new ApiExceptionDetails<>(new Date().getTime(), 404,
                "Invalid parameter", List.of(ex.getMsg())), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> HttpMessageNotReadableExceptionHandler(HttpServletRequest req, HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException{}\n", req.getRequestURI(), ex);
        return new ResponseEntity<>(new ApiExceptionDetails<>(new Date().getTime(), 404,
                "Invalid parameter", List.of(Optional.ofNullable(ex.getMessage()))), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> ExceptionHandler(HttpServletRequest req, Exception ex) {
        log.error("Exception{}\n", req.getRequestURI(), ex);
        return new ResponseEntity<>(new ApiExceptionDetails<>(new Date().getTime(), 404,
                "Invalid parameter", List.of(ex.getMessage())), HttpStatus.BAD_REQUEST);

    }
}
