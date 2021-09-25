package com.sigabem.fretecalculator.controller;

import com.sigabem.fretecalculator.model.Entrega;
import com.sigabem.fretecalculator.payload.EntregaRequest;
import com.sigabem.fretecalculator.payload.EntregaResponse;
import com.sigabem.fretecalculator.service.EntregaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.NumberFormat;
import java.util.Locale;

@RequiredArgsConstructor
@RestController
@RequestMapping("sigabem")
public class EntregaController {

    private final EntregaService service;

    @PostMapping("/calcula-frete")
    public ResponseEntity<?> postEntrega(@RequestBody EntregaRequest entregaRequest) {
        Entrega entrega = service.doEntrega(entregaRequest);
        EntregaResponse entregaResponse = toEntregaResponse(entrega);
        System.out.println(entregaResponse.toString());
        return new ResponseEntity<>(entregaResponse, HttpStatus.CREATED);
    }

    private EntregaResponse toEntregaResponse(Entrega entrega) {
        return EntregaResponse.builder()
                .cepOrigem(entrega.getCepOrigem())
                .cepDestino(entrega.getCepDestino())
                .dataPrevistaEntrega(entrega.getDataPrevistaEntrega())
                .vlTotalFrete(entrega.getVlTotalFrete())
                .build();
    }

}
