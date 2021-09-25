package com.sigabem.fretecalculator.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViaCepResponse {

    private String cep;
    private String ddd;
    private String localidade;
    private String uf;
}
