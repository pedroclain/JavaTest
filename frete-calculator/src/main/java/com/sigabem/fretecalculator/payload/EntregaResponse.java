package com.sigabem.fretecalculator.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@ApiModel
public class EntregaResponse {

    @ApiModelProperty(value = "Valor total do frete após os possiveis descontos", example = "23.23")
    private Double vlTotalFrete;
    @ApiModelProperty(value = "Data prevista para entrega no formato ISO calculada a partir dos CEPs fornecidos", example = "2021-11-12")
    private LocalDate dataPrevistaEntrega;
    @ApiModelProperty(value = "Cep de origem recebido", example = "01153000")
    private String cepOrigem;
    @ApiModelProperty(value = "Cep de destino recebido", example = "01153000")
    private String cepDestino;
}
