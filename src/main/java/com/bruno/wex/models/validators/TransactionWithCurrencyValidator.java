package com.bruno.wex.models.validators;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TransactionWithCurrencyValidator {

    public static boolean validate(String uuid, String currency) {
        if(uuid == null || uuid.length() < 24) {
            log.error("Bad uniqueId length: {}", uuid == null ? 0 : uuid.length());
            return false;
        }

        if(currency == null || currency.length() < 3) {
            log.error("Bad newCurrency length: {}", currency == null ? 0 : currency.length());
            return false;
        }
        return true;
    }
}
