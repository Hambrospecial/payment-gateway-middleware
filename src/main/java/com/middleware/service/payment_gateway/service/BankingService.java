package com.middleware.service.payment_gateway.service;

import com.middleware.service.payment_gateway.dtos.TransactionResponse;
import com.middleware.service.payment_gateway.dtos.WebhookNotification;
import com.middleware.service.payment_gateway.enums.TransactionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankingService {

    private final WebClient webClient;
    private final AsyncTaskExecutor taskExecutor;

    @Value("${webhook.path.url}")
    private String webhookPathUrl;

    public void processTransaction(TransactionResponse transactionResponse) {
        taskExecutor.execute(() -> processTransactionAsync(transactionResponse));
    }

    private void processTransactionAsync(TransactionResponse transactionResponse) {
        try {
            simulateProcessingDelay();
            sendWebhook(transactionResponse.getTransRef(), TransactionStatus.PROCESSING.getValue());

            simulateProcessingDelay();
            String finalStatus = determineFinalStatus();
            sendWebhook(transactionResponse.getTransRef(), finalStatus);

        } catch (Exception e) {
            log.error("Error processing transaction: {}", transactionResponse.getTransRef(), e);
            sendWebhook(transactionResponse.getTransRef(), TransactionStatus.REVERSED.getValue());
        }
    }

    private void sendWebhook(String transRef, String status) {
        WebhookNotification notification = new WebhookNotification(transRef, status);
        log.info("Sending webhook: {}", notification);
        webClient.post()
                .uri(webhookPathUrl)
                .bodyValue(notification)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.info("Webhook sent successfully: {}", notification))
                .doOnError(e -> log.error("Failed to send webhook: {}", notification, e))
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)))
                .subscribe();
    }

    private void simulateProcessingDelay() throws InterruptedException {
        Thread.sleep(10000);
    }

    private String determineFinalStatus() {
        return Math.random() > 0.5 ? TransactionStatus.SUCCESS.getValue() : TransactionStatus.FAILED.getValue();
    }
}

