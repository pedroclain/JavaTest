package com.sigabem.fretecalculator.service;

import com.sigabem.fretecalculator.config.exception.handler.InvalidCpfException;
import com.sigabem.fretecalculator.model.Entrega;
import com.sigabem.fretecalculator.payload.EntregaRequest;
import com.sigabem.fretecalculator.payload.ViaCepResponse;
import com.sigabem.fretecalculator.repository.EntregaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
public class EntregaServiceTest {

    @InjectMocks
    private EntregaService service;
    @Mock
    private EntregaRepository repositoryMock;
    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        Entrega entrega = Entrega.builder()
                .cepDestino("12345678")
                .cepOrigem("12345678")
                .dataConsulta(LocalDate.now())
                .nomeDestinatario("pedro")
                .peso(20D)
                .dataPrevistaEntrega(LocalDate.now().plusDays(1))
                .id(1L)
                .vlTotalFrete(10D)
                .build();

        ViaCepResponse viaCepResponse = ViaCepResponse.builder()
                .cep("12345678")
                .bairro("Copacabana")
                .ddd("21")
                .localidade("Rio de Janeiro")
                .build();

        ResponseEntity<ViaCepResponse> responseEntity = new ResponseEntity<>(viaCepResponse, HttpStatus.CREATED);
        BDDMockito.when(restTemplate.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.same(ViaCepResponse.class))).thenReturn(responseEntity);
        BDDMockito.when(repositoryMock.save(ArgumentMatchers.any())).thenReturn(entrega);
    }

    @Test
    void saveEntrega_WhenSuccessfull() {
        EntregaRequest entregaRequest = EntregaRequest.builder()
                .cepOrigem("12345678")
                .cepDestino("12345678")
                .nomeDestinatario("pedro")
                .peso(20D).build();

        Entrega expectedEntrega = Entrega.builder()
                .cepDestino("12345678")
                .cepOrigem("12345678")
                .dataConsulta(LocalDate.now())
                .nomeDestinatario("pedro")
                .peso(20D)
                .dataPrevistaEntrega(LocalDate.now().plusDays(1))
                .id(1L)
                .vlTotalFrete(10D)
                .build();

        Entrega entrega = Assertions.assertDoesNotThrow(() -> service.realizarEntrega(entregaRequest));
        Assertions.assertEquals(expectedEntrega, entrega);
    }

    @Test
    void apply50PercentOnFrete_WhenDddIsEquals() {
        EntregaRequest entregaRequest = EntregaRequest.builder()
                .cepOrigem("12345678")
                .cepDestino("12345678")
                .nomeDestinatario("pedro")
                .peso(20D).build();

        Double expectedFrete = 10D;
        Entrega entrega = Assertions.assertDoesNotThrow(() -> service.realizarEntrega(entregaRequest));
        Assertions.assertEquals(expectedFrete, entrega.getVlTotalFrete());
    }

    @Test
    void set1DayOnDataPrevistaEntrega_WhenDddIsEquals() {
        EntregaRequest entregaRequest = EntregaRequest.builder()
                .cepOrigem("12345678")
                .cepDestino("12345678")
                .nomeDestinatario("pedro")
                .peso(20D).build();


        LocalDate expectedDataPrevista = LocalDate.now().plusDays(1);
        Entrega entrega = Assertions.assertDoesNotThrow(() -> service.realizarEntrega(entregaRequest));
        Assertions.assertEquals(expectedDataPrevista, entrega.getDataPrevistaEntrega());
    }

    @Test
    void apply75PercentOnFrete_WhenLocalidadeIsEquals() {

        EntregaRequest entregaRequest = EntregaRequest.builder()
                .cepOrigem("12345678")
                .cepDestino("12345678")
                .nomeDestinatario("pedro")
                .peso(20D).build();

        ViaCepResponse viaCepOrigemResponse = ViaCepResponse.builder()
                .cep("12345678")
                .bairro("Copacabana")
                .ddd("21")
                .localidade("Rio de Janeiro")
                .build();

        ResponseEntity<ViaCepResponse> responseEntity = new ResponseEntity<>(viaCepOrigemResponse, HttpStatus.CREATED);
        ViaCepResponse viaCepDestinoResponse = ViaCepResponse.builder()
                .cep("12345678")
                .bairro("Copacabana")
                .ddd("22")
                .localidade("Rio de Janeiro")
                .build();

        ResponseEntity<ViaCepResponse> responseEntity2 = new ResponseEntity<>(viaCepOrigemResponse, HttpStatus.CREATED);
        BDDMockito.when(restTemplate.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.same(ViaCepResponse.class))).thenReturn(responseEntity).thenReturn(responseEntity2);

        Double expectedFrete = 7D;
        Entrega entrega = Assertions.assertDoesNotThrow(() -> service.realizarEntrega(entregaRequest));
        Assertions.assertEquals(expectedFrete, entrega.getVlTotalFrete());
    }

    @Test
    void whenCepOrigemIsInvalid_ThrowInvalidCepException() {
        EntregaRequest entregaRequest = EntregaRequest.builder()
                .cepOrigem("invalid")
                .cepDestino("12345678")
                .nomeDestinatario("pedro")
                .peso(20D).build();

        Assertions.assertThrows(InvalidCpfException.class, () -> service.realizarEntrega(entregaRequest));

    }

    @Test
    void whenCepDestinoIsInvalid_ThrowInvalidCepException() {
        EntregaRequest entregaRequest = EntregaRequest.builder()
                .cepOrigem("12345678")
                .cepDestino("invalid")
                .nomeDestinatario("pedro")
                .peso(20D).build();

        Assertions.assertThrows(InvalidCpfException.class, () -> service.realizarEntrega(entregaRequest));
    }
}
