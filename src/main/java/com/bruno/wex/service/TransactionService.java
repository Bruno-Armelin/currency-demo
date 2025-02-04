package com.bruno.wex.service;

import com.bruno.wex.entity.h2.TransactionEntity;
import com.bruno.wex.models.NewTransactionDTO;
import com.bruno.wex.models.NewTransactionResponseDTO;
import com.bruno.wex.models.TransactionWithCurrencyResponseDTO;
import com.bruno.wex.models.treasury.RateData;
import com.bruno.wex.repository.h2.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

@Service
@Slf4j
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public NewTransactionResponseDTO addNewTransaction(NewTransactionDTO dto) {
        try {
            var uuid = UUID.randomUUID().toString();
            var entity = new TransactionEntity(null, uuid, dto.getTransactionDate(), dto.getDescription(), dto.getPurchaseAmount().setScale(2, RoundingMode.HALF_UP));
            transactionRepository.save(entity);

            return new NewTransactionResponseDTO(dto.getTransactionDate(), dto.getDescription(), entity.getPurchaseAmount(), entity.getUuid());
        }
        catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    public TransactionEntity getTransactionFromID(String uuid) {
        return transactionRepository.getByUuid(uuid);
    }

    public TransactionWithCurrencyResponseDTO convertTransactionToCurrency(TransactionEntity entity, RateData currencyRate, String currency) {
        try {
            var exchangeRate = BigDecimal.valueOf(Double.parseDouble(currencyRate.getExchangeRate()));
            return new TransactionWithCurrencyResponseDTO(entity.getTransactionDate(), entity.getDescription(),
                    entity.getPurchaseAmount(), entity.getUuid(), currency,
                    entity.getPurchaseAmount().multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP),
                    LocalDate.parse(currencyRate.getEffectiveDate()), exchangeRate);
        }
        catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }
}
