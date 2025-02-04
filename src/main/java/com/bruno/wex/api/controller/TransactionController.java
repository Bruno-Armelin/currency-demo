package com.bruno.wex.api.controller;

import com.bruno.wex.models.NewTransactionDTO;
import com.bruno.wex.models.validators.NewTransactionValidator;
import com.bruno.wex.models.validators.TransactionWithCurrencyValidator;
import com.bruno.wex.service.TransactionService;
import com.bruno.wex.service.TreasuryReportingRatesService;
import org.apache.commons.lang3.mutable.MutableInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TreasuryReportingRatesService treasuryReportingRatesService;

    @PostMapping
    public ResponseEntity<?> newTransaction(@RequestBody NewTransactionDTO transaction) {
        if(transaction == null || !NewTransactionValidator.validate(transaction)) {
            return ResponseEntity.badRequest().build();
        }
        var responseDto = transactionService.addNewTransaction(transaction);
        if(responseDto != null) {
            return ResponseEntity.ok(responseDto);
        }
        return ResponseEntity.internalServerError().build();
    }

    private final String TRANSACTION_ERROR_FORMAT = "{\"message\":\"%s\"}";
    private final String TRANSACTION_NOT_FOUND = String.format(TRANSACTION_ERROR_FORMAT, "Transaction ID not found");
    private final String CURRENCY_NOT_FOUND = String.format(TRANSACTION_ERROR_FORMAT, "Currency exchange rate was not found");

    @GetMapping(value = "/{uuid}/{currency}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTransaction(@PathVariable("uuid") String uuid, @PathVariable("currency") String currency) {
        if(!TransactionWithCurrencyValidator.validate(uuid, currency)) {
            return ResponseEntity.badRequest().build();
        }

        var entity = transactionService.getTransactionFromID(uuid);
        if(entity == null) {
            return new ResponseEntity<>(TRANSACTION_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        var currencyRate = treasuryReportingRatesService.findCurrencyRate(currency, entity.getTransactionDate().toLocalDate());
        if(currencyRate == null) {
            return new ResponseEntity<>(CURRENCY_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        var responseDto = transactionService.convertTransactionToCurrency(entity, currencyRate, currency);
        if(responseDto != null) {
            return ResponseEntity.ok(responseDto);
        }

        return ResponseEntity.internalServerError().build();
    }
}