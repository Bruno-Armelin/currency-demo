package com.bruno.wex.models.validators;

import com.bruno.wex.models.NewTransactionDTO;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public final class NewTransactionValidator {
    private static BigDecimal ONE_CENT = BigDecimal.valueOf(0.01);

    public static boolean validate(NewTransactionDTO dto) {
        //Avoiding validations errors to be seen by remote users in prodution.
        if(dto.getTransactionDate() == null) {
            log.error("NewTransactionDTO does not contain valid date");
            return false;
        }

        if(dto.getDescription() == null || dto.getDescription().length() > 50) {
            log.error("NewTransactionDTO description is {}", dto.getDescription() == null ? "null" : "oversized");
            return false;
        }

        if(dto.getPurchaseAmount() == null || dto.getPurchaseAmount().compareTo(ONE_CENT) == -1) {
            log.error("NewTransactionDTO purchaseAmount is {}", dto.getPurchaseAmount() == null ? null : dto.getPurchaseAmount().longValue());
            return false;
        }
        return true;
    }
}
