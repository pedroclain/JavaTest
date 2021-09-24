package com.sigabem.fretecalculator.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@ToString
@Data
public class EntregaResponse {

    private Double vlTotalFrete;
    private LocalDate dataPrevistaEntrega;
    private String cepOrigem;
    private String cepDestino;
}
