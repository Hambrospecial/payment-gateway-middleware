package com.middleware.service.payment_gateway.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionRequest {
    @NotNull(message = "Transaction amount cannot be null or empty")
    @DecimalMin(value = "0.01", inclusive = false, message = "Amount must be greater than zero")
    private BigDecimal amount;
    @NotBlank(message = "Provide a transaction description")
    @Pattern(regexp = "^[a-zA-Z0-9_\\-.,\\s@!#$%&*(){}\\[\\]:\"';]+$", message = "Invalid characters in description")
    private String description;
}
