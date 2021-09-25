package com.sigabem.fretecalculator.util;

import com.sigabem.fretecalculator.model.Entrega;
import com.sigabem.fretecalculator.payload.EntregaResponse;

public class EntregaResponseUtilTest {

    public static EntregaResponse create() {
        Entrega entrega = EntregaUtilTest.create();
        return EntregaResponse.builder()
                .cepOrigem(entrega.getCepOrigem())
                .cepDestino(entrega.getCepDestino())
                .dataPrevistaEntrega(entrega.getDataPrevistaEntrega())
                .vlTotalFrete(entrega.getVlTotalFrete())
                .build();
    }

    public static EntregaResponse create50Percent() {
        Entrega entrega = EntregaUtilTest.create50Percent();
        return EntregaResponse.builder()
                .cepOrigem(entrega.getCepOrigem())
                .cepDestino(entrega.getCepDestino())
                .dataPrevistaEntrega(entrega.getDataPrevistaEntrega())
                .vlTotalFrete(entrega.getVlTotalFrete())
                .build();
    }

    public static EntregaResponse create75Percent() {
        Entrega entrega = EntregaUtilTest.create75Percent();
        return EntregaResponse.builder()
                .cepOrigem(entrega.getCepOrigem())
                .cepDestino(entrega.getCepDestino())
                .dataPrevistaEntrega(entrega.getDataPrevistaEntrega())
                .vlTotalFrete(entrega.getVlTotalFrete())
                .build();
    }
}
