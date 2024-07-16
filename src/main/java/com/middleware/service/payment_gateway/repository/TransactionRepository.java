package com.middleware.service.payment_gateway.repository;

import com.middleware.service.payment_gateway.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransRef(String transRef);
}