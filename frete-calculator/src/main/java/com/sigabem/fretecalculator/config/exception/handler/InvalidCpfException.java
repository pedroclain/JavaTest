package com.sigabem.fretecalculator.config.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class InvalidCpfException extends RuntimeException {

    private String msg;
}
