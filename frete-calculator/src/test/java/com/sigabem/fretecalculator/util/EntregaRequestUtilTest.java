package com.sigabem.fretecalculator.util;

import com.sigabem.fretecalculator.model.Entrega;
import com.sigabem.fretecalculator.payload.EntregaRequest;

public class EntregaRequestUtilTest {

    public static EntregaRequest create() {
        Entrega entrega = EntregaUtilTest.create();
        return EntregaRequest.builder()
                .cepOrigem(entrega.getCepOrigem())
                .cepDestino(entrega.getCepDestino())
                .nomeDestinatario(entrega.getNomeDestinatario())
                .peso(entrega.getPeso())
                .build();
    }

    public static EntregaRequest create50Percent() {
        EntregaRequest entregaRequest = create();
        entregaRequest.setCepOrigem("22795810");
        entregaRequest.setCepDestino("22795810");
        return entregaRequest;
    }

    public static EntregaRequest create75Percent() {
        EntregaRequest entregaRequest = create50Percent();
        entregaRequest.setCepOrigem("2390010");
        entregaRequest.setCepDestino("22795810");
        return entregaRequest;
    }
}
