package com.middleware.service.payment_gateway;

import com.middleware.service.payment_gateway.dtos.TransactionRequest;
import com.middleware.service.payment_gateway.dtos.TransactionResponse;
import com.middleware.service.payment_gateway.enums.TransactionStatus;
import com.middleware.service.payment_gateway.exception.NotFoundException;
import com.middleware.service.payment_gateway.model.Transaction;
import com.middleware.service.payment_gateway.repository.TransactionRepository;
import com.middleware.service.payment_gateway.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInitiateTransaction() {
        // Arrange
        TransactionRequest request = new TransactionRequest();
        request.setAmount(BigDecimal.valueOf(100.0));

        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(100.0));
        transaction.setStatus(TransactionStatus.PENDING.getValue());

        TransactionResponse expectedResponse = new TransactionResponse();
        expectedResponse.setTransRef("123");
        expectedResponse.setStatus(TransactionStatus.PENDING.getValue());

        when(modelMapper.map(request, Transaction.class)).thenReturn(transaction);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(modelMapper.map(transaction, TransactionResponse.class)).thenReturn(expectedResponse);

        // Act
        TransactionResponse result = transactionService.initiateTransaction(request);

        // Assert
        assertNotNull(result);
        assertEquals(TransactionStatus.PENDING.getValue(), result.getStatus());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testGetTransactionAndReturnTransaction() {
        // Arrange
        String transRef = "123";
        Transaction transaction = new Transaction();
        transaction.setTransRef(transRef);

        TransactionResponse expectedResponse = new TransactionResponse();
        expectedResponse.setTransRef(transRef);

        when(transactionRepository.findByTransRef(transRef)).thenReturn(Optional.of(transaction));
        when(modelMapper.map(transaction, TransactionResponse.class)).thenReturn(expectedResponse);

        // Act
        TransactionResponse result = transactionService.getTransaction(transRef);

        // Assert
        assertNotNull(result);
        assertEquals(transRef, result.getTransRef());
    }

    @Test
    void testGetTransactionAndThrowExceptionWhenNotFound() {
        // Arrange
        String transRef = "nonexistent";
        when(transactionRepository.findByTransRef(transRef)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> transactionService.getTransaction(transRef));
    }
}
