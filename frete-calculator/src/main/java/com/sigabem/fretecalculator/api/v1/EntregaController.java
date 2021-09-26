package com.sigabem.fretecalculator.api.v1;

import com.sigabem.fretecalculator.config.exception.ApiExceptionDetails;
import com.sigabem.fretecalculator.config.exception.InvalidParameterException;
import com.sigabem.fretecalculator.model.Entrega;
import com.sigabem.fretecalculator.payload.EntregaRequest;
import com.sigabem.fretecalculator.payload.EntregaResponse;
import com.sigabem.fretecalculator.service.EntregaService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api("Endpoint responsavel por gerenciar entregas")
@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("sigabem/api/v1/entrega")
public class EntregaController {

    private final EntregaService service;

    @ApiOperation(value = "Calcula o frete e a data prevista para entrega", response = EntregaResponse.class, code = 201)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Adiciona uma nova Entrega no banco de dados"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
            @ApiResponse(code = 400, message = "Campo invalido", response = ApiExceptionDetails.class)
    })
    @PostMapping(value = "/calcula-frete", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> postEntrega(@ApiParam(value = "Requisição para nova entrega") @RequestBody EntregaRequest entregaRequest) {
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
