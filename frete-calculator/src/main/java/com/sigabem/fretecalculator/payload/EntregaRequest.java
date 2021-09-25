package com.sigabem.fretecalculator.payload;



import com.sigabem.fretecalculator.model.Entrega;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class EntregaRequest {

    @Min(value = 0, message = "O peso náo pode ser negativo")
    @NotNull(message = "O peso não pode ser nulo")
    @NotEmpty(message = "O peso não pode ser vazio")
    private Double peso;
    @NotNull(message = "O cep de origem não pode ser nulo")
    @NotEmpty(message = "O cep de origem não pode ser vazio")
    private String cepOrigem;
    @NotNull(message = "O cep de destino não pode ser nulo")
    @NotEmpty(message = "O cep de destino não pode ser vazio")
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
