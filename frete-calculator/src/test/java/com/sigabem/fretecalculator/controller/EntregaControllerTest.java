package com.sigabem.fretecalculator.controller;

import com.sigabem.fretecalculator.model.Entrega;
import com.sigabem.fretecalculator.payload.EntregaRequest;
import com.sigabem.fretecalculator.payload.EntregaResponse;
import com.sigabem.fretecalculator.service.EntregaService;
import com.sigabem.fretecalculator.util.EntregaRequestUtilTest;
import com.sigabem.fretecalculator.util.EntregaResponseUtilTest;
import com.sigabem.fretecalculator.util.EntregaUtilTest;
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

@ExtendWith(SpringExtension.class)
public class EntregaControllerTest {

    @InjectMocks
    private EntregaController controller;
    @Mock
    private EntregaService serviceMock;

    @BeforeEach
    void setUp() {
        Entrega entrega = EntregaUtilTest.create50Percent();
        BDDMockito.when(serviceMock.doEntrega(ArgumentMatchers.any(EntregaRequest.class))).thenReturn(entrega);
    }

    @Test
    @DisplayName("When successful return status code 201 and EntregaResponse object response")
    void returnEntregaResponse_WhenSuccessful() {
        ResponseEntity<?> responseEntity = controller.postEntrega(EntregaRequestUtilTest.create50Percent());

        EntregaResponse entregaResponse = EntregaResponseUtilTest.create50Percent();
        Assertions.assertEquals(entregaResponse,responseEntity.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }
}
