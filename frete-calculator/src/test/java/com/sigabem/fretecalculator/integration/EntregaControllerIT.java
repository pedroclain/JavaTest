package com.sigabem.fretecalculator.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigabem.fretecalculator.payload.EntregaRequest;
import com.sigabem.fretecalculator.util.EntregaRequestUtilTest;
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

import java.util.HashMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class EntregaControllerIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    String URL = "/sigabem/api/v1/entrega/calcula-frete";

    @Test
    @DisplayName("When successful return status code 201 and EntregaResponse object response")
    void whenSuccessful_ReturnEntregaResponse() throws Exception{
        String requestJson = objectMapper.writeValueAsString(EntregaRequestUtilTest.create50Percent());

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("When CEP is a no numeric type throw InvalidParameterException, return custom error response and 404 status code")
    void whenCepIsInvalid_ReturnInvalidParameterException() throws Exception{
        EntregaRequest entregaRequest = EntregaRequestUtilTest.create50Percent();
        entregaRequest.setCepOrigem("1234567a");
        String jsonRequest = objectMapper.writeValueAsString(entregaRequest);
        HashMap<String, String> details = new HashMap<>();
        details.put("message", "CEP deve ser numerico no formato 'xxxxxxxx'");
        details.put("field", "cepOrigem");
        details.put("wrongValue", entregaRequest.getCepOrigem());

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Campo invalido"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details")
                        .value(details));
    }

    @Test
    @DisplayName("When CEP is a no numeric type throw InvalidParameterException, return custom error response and 404 status code")
    void whenCepIsNotFound_ReturnInvalidParameterException() throws Exception{
        EntregaRequest entregaRequest = EntregaRequestUtilTest.create50Percent();
        entregaRequest.setCepOrigem("23900100");
        String jsonRequest = objectMapper.writeValueAsString(entregaRequest);
        HashMap<String, String> details = new HashMap<>();
        details.put("message", "CEP not found");
        details.put("field", "cepOrigem");
        details.put("wrongValue", entregaRequest.getCepOrigem());

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Campo invalido"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details")
                        .value(details));
    }

    @Test
    @DisplayName("When CEP is not found in ViaCep API throw InvalidParameterException, return custom error response and 404 status code")
    void throwInvalidParameterException_WhenPesoIsInvalid() throws Exception {
        EntregaRequest entregaRequest = EntregaRequestUtilTest.create50Percent();
        entregaRequest.setPeso("-1");
        String jsonRequest = objectMapper.writeValueAsString(entregaRequest);

        HashMap<String, String> details = new HashMap<>();
        details.put("message", "Peso deve ser um valor positivo no formato 'dd.ff'");
        details.put("field", "peso");
        details.put("wrongValue", entregaRequest.getPeso());

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Campo invalido"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details")
                        .value(details));
    }
}
