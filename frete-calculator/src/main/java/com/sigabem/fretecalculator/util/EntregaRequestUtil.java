package com.sigabem.fretecalculator.util;

import com.sigabem.fretecalculator.config.exception.InvalidParameterException;
import com.sigabem.fretecalculator.payload.EntregaRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
public class EntregaRequestUtil {

    /***
     * Validate a value string. Check if is numeric, not null and 8 digits size
     * @param value: value string
     * @param fieldName: the name of field (cepOrigem/cepDestino)
     */

    private static void validateCep(String value, String fieldName) {
        String errorMsg = "CEP deve ser numerico no formato 'xxxxxxxx'";
        RuntimeException exception = new InvalidParameterException(errorMsg, fieldName, value);
        if (value == null || value.length() != 8) {
            throw exception;
        }

        try {
            Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw exception;
        }
    }

    private static void validatePeso(String value) {
        double pesoDouble;
        String errorMsg = "Peso deve ser um valor positivo no formato 'dd.ff'";
        RuntimeException exception = new InvalidParameterException(errorMsg, "peso", value);
        try {
            pesoDouble = Double.parseDouble(value);
        } catch (NumberFormatException | NullPointerException ex) {
            throw exception;
        }
        if (pesoDouble <= 0) {
            throw exception;
        }
    }

    public static void validateFields(EntregaRequest entregaRequest) {
        log.info("Validating cepOrigem. . .");
        validateCep(entregaRequest.getCepOrigem(), "cepOrigem");
        log.info("Validating cepDestino. . .");
        validateCep(entregaRequest.getCepDestino(), "cepDestino");
        validatePeso(entregaRequest.getPeso());
    }

}
