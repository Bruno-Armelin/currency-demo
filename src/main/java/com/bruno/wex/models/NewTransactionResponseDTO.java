package com.bruno.wex.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewTransactionResponseDTO {
    private LocalDateTime transactionDate;
    private String description;
    private BigDecimal purchaseAmount;
    private String uniqueId;
}
