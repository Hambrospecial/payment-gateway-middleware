package com.middleware.service.payment_gateway.service;

import com.middleware.service.payment_gateway.dtos.TransactionRequest;
import com.middleware.service.payment_gateway.dtos.TransactionResponse;
import com.middleware.service.payment_gateway.dtos.WebhookNotification;
import com.middleware.service.payment_gateway.enums.TransactionStatus;
import com.middleware.service.payment_gateway.exception.TransactionNotFoundException;
import com.middleware.service.payment_gateway.model.Transaction;
import com.middleware.service.payment_gateway.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public TransactionResponse initiateTransaction(TransactionRequest transactionRequest) {
        Transaction transaction = modelMapper.map(transactionRequest, Transaction.class);
        transaction.setStatus(TransactionStatus.PENDING.getValue());
        transaction.setTransRef(UUID.randomUUID().toString());
        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction initiated: {}", savedTransaction.getTransRef());
        return modelMapper.map(savedTransaction, TransactionResponse.class);
    }

    public TransactionResponse getTransaction(String transRef) {
        Transaction transaction = transactionRepository.findByTransRef(transRef)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with reference: " + transRef));
        return modelMapper.map(transaction, TransactionResponse.class);
    }

//    @Transactional
//    public void processWebhookNotification(WebhookNotification notification) {
//        Transaction transaction = transactionRepository.findByTransRef(notification.getTransRef())
//                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with reference: " + notification.getTransRef()));
//        transaction.setStatus(notification.getStatus());
//        transactionRepository.save(transaction);
//        log.info("Transaction status updated via webhook: {} - {}", notification.getTransRef(), notification.getStatus());
//    }

    @Transactional
    public void processWebhookNotification(WebhookNotification notification) {
        try {
            log.info("Processing webhook notification: {}", notification);
            Transaction transaction = transactionRepository.findByTransRef(notification.getTransRef())
                    .orElseThrow(() -> new TransactionNotFoundException("Transaction not found: " + notification.getTransRef()));
            transaction.setStatus(notification.getStatus());
            transactionRepository.save(transaction);
            log.info("Transaction status updated via webhook: {} - {}", notification.getTransRef(), notification.getStatus());
        } catch (Exception e) {
            log.error("Error processing webhook notification: {}", notification, e);
            throw e; // Re-throw to trigger transaction rollback
        }
    }
}
