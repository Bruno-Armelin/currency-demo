package com.bruno.wex.repository.h2;

import com.bruno.wex.entity.h2.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    TransactionEntity getByUuid(String uuid);
}
