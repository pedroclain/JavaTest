package com.sigabem.fretecalculator.payload;



import com.sigabem.fretecalculator.model.Entrega;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class EntregaRequest {
    @ApiModelProperty(value = "Deve ser um número positivo no formato \"dd.ff\"", example = "12.32")
    private String peso;
    @ApiModelProperty(value = "Deve estar no formato \"xxxxxxxx\"", example = "01153000")
    private String cepOrigem;
    @ApiModelProperty(value = "Deve estar no formato \"xxxxxxxx\"", example = "01153000")
    private String cepDestino;
    @ApiModelProperty(value = "Não é obrigatório", example = "Pedro Clain")
    private String nomeDestinatario;
}
