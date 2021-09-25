package com.sigabem.fretecalculator.service;

import com.sigabem.fretecalculator.config.exception.handler.InvalidCpfException;
import com.sigabem.fretecalculator.model.Entrega;
import com.sigabem.fretecalculator.payload.EntregaRequest;
import com.sigabem.fretecalculator.payload.ViaCepResponse;
import com.sigabem.fretecalculator.repository.EntregaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
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

    public Entrega realizarEntrega(EntregaRequest entregaRequest) {
        log.info("Validando CEP de origem. . .");
        validateCep(entregaRequest.getCepOrigem());
        log.info("Validando CEP de destino. . .");
        validateCep(entregaRequest.getCepDestino());
        String urlCepOrigem = URI + entregaRequest.getCepOrigem() + RETURN_FORMAT;
        String urlCepDestino = URI + entregaRequest.getCepDestino() + RETURN_FORMAT;
        log.info("Procurando CEPs com api ViaCep. . .");
        ResponseEntity<ViaCepResponse> responseEntityOrigin = restTemplate.getForEntity(urlCepOrigem, ViaCepResponse.class);
        ViaCepResponse viaCepResponseOrigem = responseEntityOrigin.getBody();
        log.info("CEP de origem encontrado");
        ResponseEntity<ViaCepResponse> responseEntityDestino = restTemplate.getForEntity(urlCepDestino, ViaCepResponse.class);
        log.info(""+responseEntityDestino.getStatusCode());
        ViaCepResponse viaCepResponseDestino = responseEntityDestino.getBody();
        log.info("CEP de destino encontrado");
        Entrega entrega = criarEntrega(entregaRequest, viaCepResponseOrigem, viaCepResponseDestino);
        log.info("Entrega criada com sucesso");
        return repository.save(entrega);
    }

    private Double aplicarDesconto(Double peso, ViaCepResponse viaCepResponseOrigem ,ViaCepResponse viaCepResponseDestino) {
        log.info("Aplicando desconto ao frete. . .");

        // valor do frete é igual ao valor do peso inicialmente.

        if (viaCepResponseOrigem.getDdd().equals(viaCepResponseDestino.getDdd())) {
            return peso * 0.5;
        } else if (viaCepResponseOrigem.getLocalidade().equals(viaCepResponseDestino.getLocalidade())) {
            return peso * 0.75;
        } else {
            return peso;
        }
    }

    private LocalDate calcularDataPrevista(ViaCepResponse viaCepResponseOrigem,ViaCepResponse viaCepResponseDestino) {
        log.info("calculando data de entrega prevista. . .");
        LocalDate dataPrevista = LocalDate.now();

        if (viaCepResponseOrigem.getDdd().equals(viaCepResponseDestino.getDdd())) {
            return dataPrevista.plusDays(1);
        } else if (viaCepResponseOrigem.getLocalidade().equals(viaCepResponseDestino.getLocalidade())) {
            return dataPrevista.plusDays(3);
        } else {
            return dataPrevista.plusDays(10);
        }
    }

    private Entrega criarEntrega(EntregaRequest entregaRequest, ViaCepResponse viaCepResponseOrigem, ViaCepResponse viaCepResponseDestino) {
        Entrega entrega = entregaRequest.toEntrega();
        Double frete = aplicarDesconto(entrega.getPeso(), viaCepResponseOrigem, viaCepResponseDestino);
        LocalDate dataPrevista = calcularDataPrevista(viaCepResponseOrigem, viaCepResponseDestino);
        entrega.setVlTotalFrete(frete);
        entrega.setDataPrevistaEntrega(dataPrevista);
        entrega.setDataConsulta(LocalDate.now());
        return entrega;
    }

    private void validateCep(String cep) {
        cep = cep.replace("-", "").trim();
        if (cep.length() != 8) {
            throw new InvalidCpfException(String.format("CEP %s invalido. O CEP deve ter 8 dígitos", cep));
        }

        try {
            Integer.parseInt(cep);
        } catch (NumberFormatException ex) {
            throw new InvalidCpfException(String.format("CEP %s inválido. O CPF deve ser numérico", cep));
        }
    }

}
