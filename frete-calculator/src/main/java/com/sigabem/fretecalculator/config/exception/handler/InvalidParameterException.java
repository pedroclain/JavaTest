package com.sigabem.fretecalculator.config.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InvalidParameterException extends RuntimeException{

    private String msg;
}
