package com.middleware.service.payment_gateway.controller;

import com.middleware.service.payment_gateway.dtos.TransactionRequest;
import com.middleware.service.payment_gateway.dtos.TransactionResponse;
import com.middleware.service.payment_gateway.service.BankingService;
import com.middleware.service.payment_gateway.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/retail")
@RequiredArgsConstructor
@Tag(name = "Retail", description = "Retail API for initiating transactions")
public class RetailController {

    private final TransactionService transactionService;
    private final BankingService bankingService;

    @Operation(summary = "Initiate a new transaction", description = "Creates a new transaction and starts processing it")
    @ApiResponse(responseCode = "201", description = "Transaction created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PostMapping("/transactions")
    public ResponseEntity<TransactionResponse> initiateTransaction(@RequestBody @Valid TransactionRequest transaction) {
        TransactionResponse createdTransaction = transactionService.initiateTransaction(transaction);
        // Trigger async processing without waiting for the result
        bankingService.processTransaction(createdTransaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

    @Operation(summary = "Get transaction details", description = "Retrieves the details of a specific transaction")
    @ApiResponse(responseCode = "200", description = "Transaction found")
    @ApiResponse(responseCode = "404", description = "Transaction not found")
    @GetMapping("/transactions/{transRef}")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable @NotBlank String transRef) {
        return ResponseEntity.ok(transactionService.getTransaction(transRef));
    }

}
