package com.sigabem.fretecalculator.config.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@ApiModel
public class ApiExceptionDetails<T> {
    @ApiModelProperty(example = "1632689985277")
    private Long timestamp;
    @ApiModelProperty(example = "400")
    private Integer status;
   @ApiModelProperty(example = "Campo invalido")
    private String message;
    @ApiModelProperty(example = "{field: cepDestino, message: CEP deve ser numerico no formato 'xxxxxxxx', wrongValue: 2279580q1}")
    private List<T> details;
}
