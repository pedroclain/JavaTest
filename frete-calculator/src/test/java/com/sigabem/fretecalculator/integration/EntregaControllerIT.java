package com.sigabem.fretecalculator.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigabem.fretecalculator.payload.EntregaRequest;
import com.sigabem.fretecalculator.payload.EntregaResponse;
import com.sigabem.fretecalculator.util.EntregaRequestUtilTest;
import com.sigabem.fretecalculator.util.EntregaResponseUtilTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class EntregaControllerIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("When successful return status code 201 and EntregaResponse object response")
    void returnEntregaResponse_WhenSuccessful() throws Exception{
        String requestJson = objectMapper.writeValueAsString(EntregaRequestUtilTest.create50Percent());

        mockMvc.perform(MockMvcRequestBuilders.post("/sigabem/calcula-frete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("When CEP is a no numeric type throw InvalidParameterException, return custom error response and 404 status code")
    void throwInvalidParameterException_WhenCepIsInvalid() throws Exception{
        EntregaRequest entregaRequest = EntregaRequestUtilTest.create50Percent();
        entregaRequest.setCepOrigem("1234567a");
        String jsonRequest = objectMapper.writeValueAsString(entregaRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/sigabem/calcula-frete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg")
                        .value("Invalid parameter"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details")
                        .value(String.format("CEP %s is invalid: CEP must be numeric", entregaRequest.getCepOrigem())));

        entregaRequest.setCepOrigem("123");
        jsonRequest = objectMapper.writeValueAsString(entregaRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/sigabem/calcula-frete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg")
                        .value("Invalid parameter"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details")
                        .value(String.format("CEP %s is invalid: CEP must be numeric", entregaRequest.getCepOrigem())));
    }

    @Test
    @DisplayName("When peso is empty or no positive number throw InvalidParameterException, return custom error response and 404 status code")
    void throwInvalidParameterException_WhenPesoIsInvalid() throws Exception {
        EntregaRequest entregaRequest = EntregaRequestUtilTest.create50Percent();
        entregaRequest.setPeso(-1D);
        String jsonRequest = objectMapper.writeValueAsString(entregaRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/sigabem/calcula-frete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg")
                        .value("Invalid parameter"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details")
                        .value("Peso must be a positive number"));
    }
}
