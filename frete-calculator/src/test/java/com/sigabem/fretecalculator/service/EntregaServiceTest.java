package com.sigabem.fretecalculator.service;

import com.sigabem.fretecalculator.config.exception.handler.InvalidParameterException;
import com.sigabem.fretecalculator.model.Entrega;
import com.sigabem.fretecalculator.payload.EntregaRequest;
import com.sigabem.fretecalculator.payload.ViaCepResponse;
import com.sigabem.fretecalculator.repository.EntregaRepository;
import com.sigabem.fretecalculator.util.EntregaRequestUtilTest;
import com.sigabem.fretecalculator.util.EntregaUtilTest;
import com.sigabem.fretecalculator.util.ViaCepResponseUtilTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
        Entrega entrega = Entrega.builder().build();
        ViaCepResponse viaCepResponse = ViaCepResponseUtilTest.createRioCapital();
        ResponseEntity<ViaCepResponse> responseEntity = new ResponseEntity<>(viaCepResponse, HttpStatus.CREATED);
        BDDMockito.when(restTemplate.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.same(ViaCepResponse.class))).thenReturn(responseEntity);
        BDDMockito.when(repositoryMock.save(ArgumentMatchers.any())).thenReturn(entrega);
    }

    @Test
    @DisplayName("When successful save a new Entrega object on database and return the new created Entrega object")
    void saveEntrega_WhenSuccessfull() {
        EntregaRequest entregaRequest = EntregaRequestUtilTest.create50Percent();
        Entrega expectedEntrega = EntregaUtilTest.create50Percent();

        Entrega entregaService = Assertions.assertDoesNotThrow(() -> service.doEntrega(entregaRequest));
        Assertions.assertEquals(expectedEntrega, entregaService);
    }

    @Test
    @DisplayName("When DDDs CEPs is equals, apply 50% discount in frete")
    void apply50PercentOnFrete_WhenDddsIsEquals() {
        Entrega entrega50Percent = EntregaUtilTest.create50Percent();
        EntregaRequest entregaRequest50Percent = EntregaRequestUtilTest.create50Percent();
        Entrega entregaService = Assertions.assertDoesNotThrow(() -> service.doEntrega(entregaRequest50Percent));
        Assertions.assertEquals(entrega50Percent.getVlTotalFrete(), entregaService.getVlTotalFrete());
    }

    @Test
    @DisplayName("When DDDs CEPs is equals set dataPrevistaEntrega to one days from now")
    void set1DayFromNowOnDataPrevistaEntrega_WhenDddsIsEquals() {
        Entrega entrega50Percent = EntregaUtilTest.create50Percent();
        EntregaRequest entregaRequest50Percent = EntregaRequestUtilTest.create50Percent();

        Entrega entregaService = Assertions.assertDoesNotThrow(() -> service.doEntrega(entregaRequest50Percent));
        Assertions.assertEquals(entrega50Percent.getDataPrevistaEntrega(), entregaService.getDataPrevistaEntrega());
    }

    @Test
    @DisplayName("When DDDs is different but localidades is iquals, apply 75% discount in frete")
    void apply75PercentOnFrete_WhenLocalidadesIsEquals() {
        ViaCepResponse viaCepResponseRJ = ViaCepResponseUtilTest.createRioCapital();
        ViaCepResponse viaCepResponseSG = ViaCepResponseUtilTest.createRioAngra();

        Entrega entrega75Percent = EntregaUtilTest.create75Percent();
        EntregaRequest entregaRequest75Percent = EntregaRequestUtilTest.create75Percent();

        ResponseEntity<ViaCepResponse> responseEntityRJ = new ResponseEntity<>(viaCepResponseRJ, HttpStatus.CREATED);
        ResponseEntity<ViaCepResponse> responseEntitySG = new ResponseEntity<>(viaCepResponseSG, HttpStatus.CREATED);

        BDDMockito.when(restTemplate.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.same(ViaCepResponse.class)))
                .thenReturn(responseEntityRJ).thenReturn(responseEntitySG);

        Entrega entregaService = Assertions.assertDoesNotThrow(() -> service.doEntrega(entregaRequest75Percent));
        Assertions.assertEquals(entrega75Percent.getVlTotalFrete(), entregaService.getVlTotalFrete());
    }
//
    @Test
    @DisplayName("When DDDs is different but localidades is iquals, set dataPrevistaEntrega to three days from now")
    void set3DayFromNowOnDataPrevistaEntrega_WhenLocalidadesIsEquals() {
        ViaCepResponse viaCepResponseRJ = ViaCepResponseUtilTest.createRioCapital();
        ViaCepResponse viaCepResponseAG = ViaCepResponseUtilTest.createRioAngra();

        Entrega entrega75Percent = EntregaUtilTest.create75Percent();
        EntregaRequest entregaRequest75Percent = EntregaRequestUtilTest.create75Percent();

        ResponseEntity<ViaCepResponse> responseEntityRJ = new ResponseEntity<>(viaCepResponseRJ, HttpStatus.CREATED);
        ResponseEntity<ViaCepResponse> responseEntityAG = new ResponseEntity<>(viaCepResponseAG, HttpStatus.CREATED);

        BDDMockito.when(restTemplate.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.same(ViaCepResponse.class)))
                .thenReturn(responseEntityRJ).thenReturn(responseEntityAG);

        Entrega entregaService = Assertions.assertDoesNotThrow(() -> service.doEntrega(entregaRequest75Percent));
        Assertions.assertEquals(entrega75Percent.getDataPrevistaEntrega(), entregaService.getDataPrevistaEntrega());
    }

    @Test
    @DisplayName("When cepOrigem is invalid throw InvalidParameterException")
    void whenCepOrigemIsInvalid_ThrowInvalidParameterException() {
        EntregaRequest entregaRequest = EntregaRequestUtilTest.create50Percent();

        entregaRequest.setCepOrigem("abc123");
        InvalidParameterException invalidParameterException = Assertions.assertThrows(InvalidParameterException.class, () -> service.doEntrega(entregaRequest));
        Assertions.assertEquals(String.format("CEP %s is invalid: CEP must have 8 digits",
                entregaRequest.getCepOrigem()), invalidParameterException.getMsg());

        entregaRequest.setCepOrigem(null);
        invalidParameterException = Assertions.assertThrows(InvalidParameterException.class, () -> service.doEntrega(entregaRequest));
        Assertions.assertEquals(String.format("CEP %s is invalid: CEP must have 8 digits",
                entregaRequest.getCepOrigem()), invalidParameterException.getMsg());
    }
//
    @Test
    @DisplayName("When cepDestino is invalid throw InvalidParameterException")
    void whenCepDestinoIsInvalid_ThrowInvalidParameterException() {
        EntregaRequest entregaRequest = EntregaRequestUtilTest.create50Percent();
        entregaRequest.setCepDestino("abc123");
        InvalidParameterException invalidParameterException = Assertions.assertThrows(InvalidParameterException.class, () -> service.doEntrega(entregaRequest));
        Assertions.assertEquals(String.format("CEP %s is invalid: CEP must have 8 digits",
                entregaRequest.getCepDestino()), invalidParameterException.getMsg());
    }
//
    @Test
    @DisplayName("When CEP is no numeric type throw InvalidParameterException")
    void whenCepIsNonNumeric_ThrowInvalidParameterException() {
        EntregaRequest entregaRequest = EntregaRequestUtilTest.create50Percent();
        entregaRequest.setCepOrigem("1234567a");
        InvalidParameterException invalidParameterException = Assertions.assertThrows(InvalidParameterException.class, () -> service.doEntrega(entregaRequest));
        Assertions.assertEquals(String.format("CEP %s is invalid: CEP must be numeric",
                entregaRequest.getCepOrigem()), invalidParameterException.getMsg());
    }
//
    @Test
    @DisplayName("When CEP is no 8 size throw InvalidParameterException")
    void whenCepIsNon8Size_ThrowInvalidParameterException() {
        EntregaRequest entregaRequest = EntregaRequestUtilTest.create50Percent();
        entregaRequest.setCepOrigem("123");
        InvalidParameterException invalidParameterException = Assertions.assertThrows(InvalidParameterException.class, () -> service.doEntrega(entregaRequest));
        Assertions.assertEquals(String.format("CEP %s is invalid: CEP must have 8 digits",
                entregaRequest.getCepOrigem()), invalidParameterException.getMsg());
    }
//
    @Test
    @DisplayName("When peso is null or no positive number throw InvalidParameterException")
    void whenPesoIsInvalid_ThrowInvalidParameterException() {
        EntregaRequest entregaRequest = EntregaRequestUtilTest.create50Percent();

        entregaRequest.setPeso(-1D);
        InvalidParameterException invalidParameterException = Assertions.assertThrows(InvalidParameterException.class, () -> service.doEntrega(entregaRequest));
        Assertions.assertEquals("Peso must be a positive number", invalidParameterException.getMsg());

        entregaRequest.setPeso(null);
        invalidParameterException = Assertions.assertThrows(InvalidParameterException.class, () -> service.doEntrega(entregaRequest));
        Assertions.assertEquals("Peso must be a positive number", invalidParameterException.getMsg());

        entregaRequest.setPeso(0D);
        invalidParameterException = Assertions.assertThrows(InvalidParameterException.class, () -> service.doEntrega(entregaRequest));
        Assertions.assertEquals("Peso must be a positive number", invalidParameterException.getMsg());
    }
//
    @Test
    @DisplayName("When CEP is not found throw InvalidParameterException")
    void whenCepNotFound_InvalidParameterException() {
        ResponseEntity<ViaCepResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.CREATED);
        BDDMockito.when(restTemplate.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.same(ViaCepResponse.class))).thenReturn(responseEntity);

        EntregaRequest entregaRequest = EntregaRequestUtilTest.create50Percent();
        InvalidParameterException invalidParameterException = Assertions.assertThrows(InvalidParameterException.class, () -> service.doEntrega(entregaRequest));
        Assertions.assertEquals(String.format("cepOrigem = %s not found", entregaRequest.getCepOrigem()), invalidParameterException.getMsg());
    }

    @Test
    @DisplayName("When CEPs is from different states no apply discount")
    void applyNoDiscountOnFrete_WhenLocalidadesIsDifferent() {
        Entrega entrega = EntregaUtilTest.create();
        ViaCepResponse viaCepResponseRJ = ViaCepResponseUtilTest.createRioCapital();
        ViaCepResponse viaCepResponseSP = ViaCepResponseUtilTest.createSaoPaulo();
        ResponseEntity<ViaCepResponse> responseEntityRJ = new ResponseEntity<>(viaCepResponseRJ, HttpStatus.CREATED);
        ResponseEntity<ViaCepResponse> responseEntitySP = new ResponseEntity<>(viaCepResponseSP, HttpStatus.CREATED);

        BDDMockito.when(restTemplate.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.same(ViaCepResponse.class)))
                .thenReturn(responseEntityRJ).thenReturn(responseEntitySP);
        BDDMockito.when(repositoryMock.save(ArgumentMatchers.any())).thenReturn(entrega);

        EntregaRequest entregaRequest = EntregaRequestUtilTest.create();
        Entrega entregaService = Assertions.assertDoesNotThrow(() -> service.doEntrega(entregaRequest));
        Assertions.assertEquals(entrega.getVlTotalFrete(), entregaService.getVlTotalFrete());
    }

    @Test
    @DisplayName("When CEPs is from different states set DataPrevistaEntrega to ten days from now")
    void set10DayFromNowOnDataPrevistaEntrega_WhenLocalidadesIsDifferent() {
        Entrega entrega = EntregaUtilTest.create();
        ViaCepResponse viaCepResponseRJ = ViaCepResponseUtilTest.createRioCapital();
        ViaCepResponse viaCepResponseSP = ViaCepResponseUtilTest.createSaoPaulo();
        ResponseEntity<ViaCepResponse> responseEntityRJ = new ResponseEntity<>(viaCepResponseRJ, HttpStatus.CREATED);
        ResponseEntity<ViaCepResponse> responseEntitySP = new ResponseEntity<>(viaCepResponseSP, HttpStatus.CREATED);

        BDDMockito.when(restTemplate.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.same(ViaCepResponse.class)))
                .thenReturn(responseEntityRJ).thenReturn(responseEntitySP);
        BDDMockito.when(repositoryMock.save(ArgumentMatchers.any())).thenReturn(entrega);

        EntregaRequest entregaRequest = EntregaRequestUtilTest.create();
        Entrega entregaService = Assertions.assertDoesNotThrow(() -> service.doEntrega(entregaRequest));
        Assertions.assertEquals(entrega.getDataPrevistaEntrega(), entregaService.getDataPrevistaEntrega());
    }
}
