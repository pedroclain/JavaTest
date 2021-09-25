package com.sigabem.fretecalculator.util;

import com.sigabem.fretecalculator.model.Entrega;

import java.time.LocalDate;

public class EntregaUtilTest {

    public static Entrega create() {
        return Entrega.builder()
                .cepDestino("22795810")
                .cepOrigem("01153000") // Sao Paulo
                .dataConsulta(LocalDate.now())
                .nomeDestinatario("pedro")
                .peso(20D)
                .dataPrevistaEntrega(LocalDate.now().plusDays(10))
                .vlTotalFrete(20D)
                .build();
    }

    public static Entrega create50Percent() {
        Entrega entrega = create();
        entrega.setCepOrigem("22795810");
        entrega.setCepDestino("22795810");
        entrega.setDataPrevistaEntrega(LocalDate.now().plusDays(1));
        entrega.setVlTotalFrete(10D);
        return entrega;
    }

    public static Entrega create75Percent() {
        Entrega entrega = create();
        entrega.setCepOrigem("22795810");
        entrega.setCepDestino("2390010"); // Angra
        entrega.setDataPrevistaEntrega(LocalDate.now().plusDays(3));
        entrega.setVlTotalFrete(5D);
        return entrega;
    }
}
