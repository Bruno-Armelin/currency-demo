package com.bruno.wex.entity.h2;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "transactions")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; ////Keeps the ID numeric for fast indexing.

    @Column(unique = true, length = 64)
    private String uuid; //Only uses uuid for communication so that an attacker can't loop our database.

    private LocalDateTime transactionDate;

    @Column(length = 50)
    private String description;

    private BigDecimal purchaseAmount;
}
