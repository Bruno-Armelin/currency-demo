package com.bruno.wex.api.service;

import com.bruno.wex.entity.h2.TransactionEntity;
import com.bruno.wex.models.NewTransactionDTO;
import com.bruno.wex.repository.h2.TransactionRepository;
import com.bruno.wex.service.TransactionService;
import com.bruno.wex.service.TreasuryReportingRatesService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TreasuryReportingRatesService treasuryReportingRatesService;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void TransactionService_CreateTransactionRoundingUp() {
        var date = LocalDateTime.of(2025, 1, 1, 10, 1, 10);
        var dto = new NewTransactionDTO(date, "Service Test 1", BigDecimal.valueOf(50.012));
        var result = transactionService.addNewTransaction(dto);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(UUID.fromString(result.getUniqueId())).isNotNull();
        Assertions.assertThat(result.getPurchaseAmount().compareTo(BigDecimal.valueOf(50.01D))).isEqualTo(0);
    }

    @Test
    public void TransactionService_CreateTransactionRoundingDown() {
        var date = LocalDateTime.of(2025, 1, 1, 10, 1, 10);
        var dto = new NewTransactionDTO(date, "Service Test 2", BigDecimal.valueOf(50.015));
        var result = transactionService.addNewTransaction(dto);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getUniqueId().length()).isGreaterThan(20);
        Assertions.assertThat(result.getPurchaseAmount().compareTo(BigDecimal.valueOf(50.02D))).isEqualTo(0);
    }

    @Test
    public void TransactionService_TestCurrencyWithExchangeRate() {
        var date = LocalDateTime.of(2025, 1, 1, 10, 1, 10);
        var entity = new TransactionEntity(1L, "Test2", date, "Retrieve Test 1", BigDecimal.valueOf(10.5));

        when(transactionRepository.getByUuid(entity.getUuid())).thenReturn(entity);

        var resultEntity = transactionService.getTransactionFromID("Test2");
        var rate = treasuryReportingRatesService.findCurrencyRate("Brazil-Real", entity.getTransactionDate().toLocalDate());
        Assertions.assertThat(rate).isNotNull();

        var result = transactionService.convertTransactionToCurrency(resultEntity, rate, "Brazil-Real");
        Assertions.assertThat(result.getRateReferenceDate()).isEqualTo(LocalDate.of(2024, 12, 31));
        Assertions.assertThat(result.getExchangeRate().compareTo(BigDecimal.valueOf(6.184))).isEqualTo(0);
        Assertions.assertThat(result.getNewPurchaseAmount().compareTo(BigDecimal.valueOf(64.93))).isEqualTo(0);
    }

    @Test
    public void TransactionService_TestCurrencyWithoutExchangeRate() {
        var date = LocalDateTime.of(2001, 3, 30, 10, 1, 10);
        var entity = new TransactionEntity(1L, "Test2", date, "Retrieve Test 1", BigDecimal.valueOf(10.5));

        var rate = treasuryReportingRatesService.findCurrencyRate("Canada-Dollar", entity.getTransactionDate().toLocalDate());
        Assertions.assertThat(rate).isNull();
    }

    @Test
    public void TransactionService_TestCurrencyWithExchangeRateInExactDate() {
        var date = LocalDateTime.of(2001, 3, 31, 10, 1, 10);
        var entity = new TransactionEntity(1L, "Test2", date, "Retrieve Test 1", BigDecimal.valueOf(10.5));

        var rate = treasuryReportingRatesService.findCurrencyRate("Argentina-Peso", entity.getTransactionDate().toLocalDate());
        Assertions.assertThat(rate).isNotNull();
        Assertions.assertThat(rate.getEffectiveDate()).isEqualTo(date.toLocalDate().toString());
    }
}
