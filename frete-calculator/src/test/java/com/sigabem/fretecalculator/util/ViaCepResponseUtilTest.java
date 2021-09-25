package com.sigabem.fretecalculator.util;

import com.sigabem.fretecalculator.payload.ViaCepResponse;

public class ViaCepResponseUtilTest {

    public static ViaCepResponse createRioCapital() {
        return ViaCepResponse.builder()
                .cep("22795810")
                .uf("RJ")
                .ddd("21")
                .localidade("Rio de Janeiro")
                .build();
    }

    public static ViaCepResponse createRioAngra() {
        return ViaCepResponse.builder()
                .cep("2390010")
                .uf("RJ")
                .ddd("24")
                .localidade("Angra dos Reis")
                .build();
    }

    public static ViaCepResponse createSaoPaulo() {
        return ViaCepResponse.builder()
                .cep("01153000")
                .uf("SP")
                .ddd("11")
                .localidade("Sao Paulo")
                .build();
    }
}
