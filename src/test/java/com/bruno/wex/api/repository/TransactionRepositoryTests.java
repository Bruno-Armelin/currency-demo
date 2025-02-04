package com.bruno.wex.api.repository;

import com.bruno.wex.entity.h2.TransactionEntity;
import com.bruno.wex.repository.h2.TransactionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TransactionRepositoryTests {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void TransactionRepository_TestAutoIncrement() {
        var date = LocalDateTime.of(2025, 1, 1, 10, 1, 10);
        var entity = new TransactionEntity(null, "Test1UUID", date, "Test 1", BigDecimal.valueOf(100D));
        transactionRepository.save(entity);

        Assertions.assertThat(entity.getId()).isGreaterThan(0);
    }

    @Test
    public void TransactionRepository_TestDescriptionLength() {
        var date = LocalDateTime.of(2025, 1, 1, 10, 1, 10);
        var sb = new StringBuilder();
        for(int i = 0; i < 100; i++) sb.append("a");

        var entity = new TransactionEntity(null, sb.toString(), date, "Test 2", BigDecimal.valueOf(100D));
        try {
            transactionRepository.save(entity);
        } catch (Exception e) {}

        Assertions.assertThat(entity.getId()).isNull();
    }

    @Test
    public void TransactionRepository_TestLookup() {
        TransactionRepository_TestAutoIncrement();
        var entity = transactionRepository.getByUuid("Test1UUID");
        Assertions.assertThat(entity.getDescription()).isEqualTo("Test 1");
        Assertions.assertThat(entity.getPurchaseAmount()).isEqualTo(BigDecimal.valueOf(100D));
    }
}
