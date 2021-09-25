package com.sigabem.fretecalculator.config.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiExceptionDetails<T> {

    private Long timestamp;
    private Integer status;
    private String message;
    private List<T> details;
}
