package com.bruno.wex.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewTransactionDTO {
    @DateTimeFormat(pattern = "yyyy-mm-ddTHH:mm:ss")
    private LocalDateTime transactionDate;
    private String description;
    private BigDecimal purchaseAmount;
}
