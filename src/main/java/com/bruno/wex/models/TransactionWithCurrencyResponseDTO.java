package com.bruno.wex.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionWithCurrencyResponseDTO {
    private LocalDateTime transactionDate;
    private String description;
    private BigDecimal purchaseAmount;
    private String uniqueId;
    private String newCurrency;
    private BigDecimal newPurchaseAmount;
    private LocalDate rateReferenceDate;
    private BigDecimal exchangeRate;
}
