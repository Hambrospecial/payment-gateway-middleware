package com.middleware.service.payment_gateway.controller;

import com.middleware.service.payment_gateway.dtos.TransactionResponse;
import com.middleware.service.payment_gateway.dtos.WebhookNotification;
import com.middleware.service.payment_gateway.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Transactions", description = "Transaction APIs for initiating Payment")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Get transaction details", description = "Retrieves the details of a specific transaction")
    @ApiResponse(responseCode = "200", description = "Transaction found")
    @ApiResponse(responseCode = "404", description = "Transaction not found")
    @GetMapping("/{transRef}")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable @NotBlank String transRef) {
        TransactionResponse response = transactionService.getTransaction(transRef);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Receive webhook notification", description = "Processes a webhook notification for transaction status updates")
    @ApiResponse(responseCode = "200", description = "Webhook processed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PostMapping("/webhook")
    public ResponseEntity<String> receiveWebhook(@Validated @Valid @RequestBody WebhookNotification notification) {
            log.info("Received webhook notification: {}", notification);
            transactionService.processWebhookNotification(notification);
            return ResponseEntity.ok("Webhook processed successfully");
    }
}

