package com.sigabem.fretecalculator.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Double peso;
    @Column(nullable = false)
    private String cepOrigem;
    @Column(nullable = false)
    private String cepDestino;
    protected String nomeDestinatario;
    @Column(nullable = false)
    private Double vlTotalFrete;
    @Column(nullable = false)
    private LocalDate dataPrevistaEntrega;
    @Column(nullable = false)
    private LocalDate dataConsulta;
}
