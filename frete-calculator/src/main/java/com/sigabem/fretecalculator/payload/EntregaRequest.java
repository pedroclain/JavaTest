package com.sigabem.fretecalculator.payload;



import com.sigabem.fretecalculator.model.Entrega;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class EntregaRequest {

    private Double peso;
    @NotNull(message = "O cep de origem não pode ser nulo")
    @NotEmpty(message = "O cep de origem não pode ser vazio")
    private String cepOrigem;
    @NotNull(message = "O cep de origem não pode ser nulo")
    @NotEmpty(message = "O cep de origem não pode ser vazio")
    private String cepDestino;
    private String nomeDestinatario;

    public Entrega toEntrega() {
        return Entrega.builder()
                .cepOrigem(cepOrigem)
                .cepDestino(cepDestino)
                .peso(peso)
                .nomeDestinatario(nomeDestinatario)
                .build();
    }
}
