package com.sigabem.fretecalculator.config.exception.handler;

import com.sigabem.fretecalculator.config.exception.ApiExceptionDetails;
import com.sigabem.fretecalculator.config.exception.InvalidParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@ControllerAdvice
@Slf4j
public class GlobalApiExceptionHandler {

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<?> InvalidParameterExceptionHandler(HttpServletRequest req, InvalidParameterException ex) {
        log.error("InvalidParameterException: {}. {}\n", req.getRequestURI(), ex.getMsg(), ex);
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("message", ex.getMsg());
        errorDetails.put("field", ex.getField());
        errorDetails.put("wrongValue", ex.getWrongValue());
        ApiExceptionDetails<Object> exceptionDetails = ApiExceptionDetails.builder()
                .timestamp(new Date().getTime())
                .message("Campo invalido")
                .status(HttpStatus.BAD_REQUEST.value())
                .details(List.of(errorDetails))
                .build();
        return new ResponseEntity<>(exceptionDetails, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> ExceptionHandler(HttpServletRequest req, Exception ex) {
        log.error("Exception{}\n", req.getRequestURI(), ex);
        return new ResponseEntity<>(new ApiExceptionDetails<>(new Date().getTime(), 500,
                "Ocorreu uma exceção", List.of(ex.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
