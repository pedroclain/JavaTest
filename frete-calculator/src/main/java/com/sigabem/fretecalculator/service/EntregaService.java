package com.sigabem.fretecalculator.service;

import com.sigabem.fretecalculator.config.exception.handler.InvalidParameterException;
import com.sigabem.fretecalculator.model.Entrega;
import com.sigabem.fretecalculator.payload.EntregaRequest;
import com.sigabem.fretecalculator.payload.ViaCepResponse;
import com.sigabem.fretecalculator.repository.EntregaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntregaService {
    private final EntregaRepository repository;
    private final RestTemplate restTemplate;
    private final String URI = "https://viacep.com.br/ws/";
    private final String RETURN_FORMAT = "/json/";

    public Entrega doEntrega(EntregaRequest entregaRequest) {
        log.info("Validando CEP de origem. . .");
        validateCep(entregaRequest.getCepOrigem());
        log.info("Validando CEP de destino. . .");
        validateCep(entregaRequest.getCepDestino());
        validatePeso(entregaRequest.getPeso());
        String urlCepOrigem = URI + entregaRequest.getCepOrigem() + RETURN_FORMAT;
        String urlCepDestino = URI + entregaRequest.getCepDestino() + RETURN_FORMAT;

        log.info("Procurando CEPs com api ViaCep. . .");
        ResponseEntity<ViaCepResponse> responseEntityOrigin = restTemplate.getForEntity(urlCepOrigem, ViaCepResponse.class);
        ViaCepResponse viaCepResponseOrigem = responseEntityOrigin.getBody();
        if (viaCepResponseOrigem == null) throw new InvalidParameterException(String.format("cepOrigem = %s not found", entregaRequest.getCepOrigem()));
        log.info("CEP de origem encontrado");

        ResponseEntity<ViaCepResponse> responseEntityDestino = restTemplate.getForEntity(urlCepDestino, ViaCepResponse.class);
        log.info(""+responseEntityDestino.getStatusCode());
        ViaCepResponse viaCepResponseDestino = responseEntityDestino.getBody();
        if (viaCepResponseDestino == null) throw new InvalidParameterException(String.format("cepDestino = %s not found", entregaRequest.getCepDestino()));
        log.info("CEP de destino encontrado");
        System.out.println(viaCepResponseOrigem);
        System.out.println(viaCepResponseDestino);
        Entrega entrega = criarEntrega(entregaRequest, viaCepResponseOrigem, viaCepResponseDestino);
        log.info("Entrega criada com sucesso");
        repository.save(entrega);
        return entrega;
    }

    private Double aplicarDesconto(Double peso, ViaCepResponse viaCepResponseOrigem ,ViaCepResponse viaCepResponseDestino) {
        log.info("Aplicando desconto ao frete. . .");

        // valor do frete é igual ao valor do peso inicialmente.

        if (viaCepResponseOrigem.getDdd().equals(viaCepResponseDestino.getDdd())) {
            return peso * 0.5;
        } else if (viaCepResponseOrigem.getUf().equals(viaCepResponseDestino.getUf())) {
            return peso * 0.25;
        } else {
            return peso;
        }
    }

    private LocalDate calcularDataPrevista(ViaCepResponse viaCepResponseOrigem,ViaCepResponse viaCepResponseDestino) {
        log.info("calculando data de entrega prevista. . .");
        LocalDate dataPrevista = LocalDate.now();

        if (viaCepResponseOrigem.getDdd().equals(viaCepResponseDestino.getDdd())) {
            return dataPrevista.plusDays(1);
        } else if (viaCepResponseOrigem.getUf().equals(viaCepResponseDestino.getUf())) {
            return dataPrevista.plusDays(3);
        } else {
            return dataPrevista.plusDays(10);
        }
    }

    private Entrega criarEntrega(EntregaRequest entregaRequest, ViaCepResponse viaCepResponseOrigem, ViaCepResponse viaCepResponseDestino) {
        Double frete = aplicarDesconto(entregaRequest.getPeso(), viaCepResponseOrigem, viaCepResponseDestino);
        LocalDate dataPrevista = calcularDataPrevista(viaCepResponseOrigem, viaCepResponseDestino);
        return Entrega.builder()
                .cepOrigem(entregaRequest.getCepOrigem())
                .cepDestino(entregaRequest.getCepDestino())
                .peso(entregaRequest.getPeso())
                .nomeDestinatario(entregaRequest.getNomeDestinatario())
                .vlTotalFrete(frete)
                .dataPrevistaEntrega(dataPrevista)
                .dataConsulta(LocalDate.now())
                .build();
    }

    private void validateCep(String cep) {
        if (cep == null) {
            throw new InvalidParameterException(String.format("CEP %s is invalid: CEP must have 8 digits", cep));
        }

        cep = cep.replace("-", "").trim();
        if (cep.length() != 8) {
            throw new InvalidParameterException(String.format("CEP %s is invalid: CEP must have 8 digits", cep));
        }

        try {
            Integer.parseInt(cep);
        } catch (NumberFormatException ex) {
            throw new InvalidParameterException(String.format("CEP %s is invalid: CEP must be numeric", cep));
        }
    }

    private void validatePeso(Double peso) {
        if (peso == null || peso <= 0) {
            throw new InvalidParameterException("Peso must be a positive number");
        }
    }

}
