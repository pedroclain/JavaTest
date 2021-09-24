package com.sigabem.fretecalculator.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ViaCepResponse {

    private String cep;
    private String ddd;
    private String localidade;
    private String bairro;
}
