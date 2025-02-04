package com.bruno.wex.api.controller;

import com.bruno.wex.models.NewTransactionDTO;
import com.bruno.wex.models.NewTransactionResponseDTO;
import com.bruno.wex.models.TransactionWithCurrencyResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void TransactionController_CreateTransaction() throws Exception {
        var obj = new NewTransactionDTO(
                LocalDateTime.of(2025, 1, 1, 23, 1, 2),
                "test 1",
                BigDecimal.valueOf(10.123));

        var result = restTemplate.postForEntity("http://localhost:" + port + "/transactions", obj, NewTransactionResponseDTO.class);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(result.getBody()).isNotNull();
        Assertions.assertThat(result.getBody().getUniqueId().length()).isGreaterThan(20);
        Assertions.assertThat(result.getBody().getPurchaseAmount().compareTo(BigDecimal.valueOf(10.12))).isEqualTo(0);
    }

    @Test
    public void TransactionController_SearchInvalidTransaction() throws Exception {
        var result = restTemplate.getForEntity("http://localhost:" + port + "/transactions/1b341eec-8efe-4d06-8466-6a23c38ff6601/Brazil-Real", TransactionWithCurrencyResponseDTO.class);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void TransactionController_SearchBadCurrency() throws Exception {
        var obj = new NewTransactionDTO(
                LocalDateTime.of(2024, 6, 5, 23, 1, 2),
                "test 1",
                BigDecimal.valueOf(10.123));

        var transactionResult = restTemplate.postForEntity("http://localhost:" + port + "/transactions", obj, NewTransactionResponseDTO.class);
        var result = restTemplate.getForEntity("http://localhost:" + port + "/transactions/"+transactionResult.getBody().getUniqueId()+"/Brazilian-Real", String.class);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(result.getBody()).contains("Currency");
    }

    @Test
    public void TransactionController_TestValidCurrency() throws Exception {
        var obj = new NewTransactionDTO(
                LocalDateTime.of(2024, 6, 5, 23, 1, 2),
                "test 1",
                BigDecimal.valueOf(10.123));

        var transactionResult = restTemplate.postForEntity("http://localhost:" + port + "/transactions", obj, NewTransactionResponseDTO.class);
        var result = restTemplate.getForEntity("http://localhost:" + port + "/transactions/"+transactionResult.getBody().getUniqueId()+"/Brazil-Real", TransactionWithCurrencyResponseDTO.class);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(result.getBody().getNewPurchaseAmount().compareTo(BigDecimal.valueOf(50.75))).isEqualTo(0);
    }

}
