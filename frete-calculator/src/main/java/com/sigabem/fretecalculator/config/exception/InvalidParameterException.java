package com.sigabem.fretecalculator.config.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InvalidParameterException extends RuntimeException {

    private String msg;
    private String field;
    private String wrongValue;
}
