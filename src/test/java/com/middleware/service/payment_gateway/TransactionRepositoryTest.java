package com.middleware.service.payment_gateway;

import com.middleware.service.payment_gateway.enums.TransactionStatus;
import com.middleware.service.payment_gateway.model.Transaction;
import com.middleware.service.payment_gateway.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void testFindByTransRefAndReturnTransaction() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setTransRef("123");
        transaction.setAmount(BigDecimal.valueOf(100.0));
        transaction.setStatus(TransactionStatus.PENDING.getValue());
        entityManager.persist(transaction);
        entityManager.flush();
        // Act
        Transaction found = transactionRepository.findByTransRef("123").orElse(null);
        // Assert
        assertNotNull(found);
        assertEquals("123", found.getTransRef());
    }
}
