package com.sigabem.fretecalculator.service;

import com.sigabem.fretecalculator.config.exception.InvalidParameterException;
import com.sigabem.fretecalculator.model.Entrega;
import com.sigabem.fretecalculator.payload.EntregaRequest;
import com.sigabem.fretecalculator.payload.ViaCepResponse;
import com.sigabem.fretecalculator.repository.EntregaRepository;
import com.sigabem.fretecalculator.util.EntregaRequestUtil;
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

        EntregaRequestUtil.validateFields(entregaRequest);
        String cepOrigem = entregaRequest.getCepOrigem();
        String cepDestino = entregaRequest.getCepDestino();
        String urlCepOrigem = URI + cepOrigem + RETURN_FORMAT;
        String urlCepDestino = URI + cepDestino + RETURN_FORMAT;

        log.info("Searching CEPs with ViaCep API. . .");
        ViaCepResponse viaCepResponseOrigem = restTemplate.getForEntity(urlCepOrigem, ViaCepResponse.class).getBody();

        if (viaCepResponseOrigem.getDdd() == null) throw new InvalidParameterException("CEP not found", "cepOrigem", cepOrigem);
        log.info(String.format("CEP '%s' found with values = %s", cepOrigem, viaCepResponseOrigem));

        ViaCepResponse viaCepResponseDestino = restTemplate.getForEntity(urlCepDestino, ViaCepResponse.class).getBody();
        if (viaCepResponseDestino.getDdd() == null) throw new InvalidParameterException("CEP not found", "cepDestino", cepDestino);
        log.info(String.format("CEP '%s' found with values = %s", cepDestino, viaCepResponseDestino));

        Entrega entrega = criarEntrega(entregaRequest, viaCepResponseOrigem, viaCepResponseDestino);
        log.info("New Entrega object created and persisted on database");

        repository.save(entrega);
        return entrega;
    }

    private Double aplicarDesconto(Double peso, ViaCepResponse viaCepResponseOrigem ,ViaCepResponse viaCepResponseDestino) {
        log.info("Aplicando desconto ao frete. . .");

        // frete value equals peso value initially

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
        double peso = Double.parseDouble(entregaRequest.getPeso());
        Double frete = aplicarDesconto(peso, viaCepResponseOrigem, viaCepResponseDestino);
        LocalDate dataPrevista = calcularDataPrevista(viaCepResponseOrigem, viaCepResponseDestino);
        return Entrega.builder()
                .cepOrigem(entregaRequest.getCepOrigem())
                .cepDestino(entregaRequest.getCepDestino())
                .peso(peso)
                .nomeDestinatario(entregaRequest.getNomeDestinatario())
                .vlTotalFrete(frete)
                .dataPrevistaEntrega(dataPrevista)
                .dataConsulta(LocalDate.now())
                .build();
    }



}
