package com.sigabem.fretecalculator.payload;



import com.sigabem.fretecalculator.model.Entrega;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntregaRequest {
    private Double peso;
    private String cepOrigem;
    private String cepDestino;
    private String nomeDestinatario;
}
