package com.middleware.service.payment_gateway.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum TransactionStatus {

    PENDING("PENDING", "Transaction is awaiting processing"),
    PROCESSING("PROCESSING", "Transaction is currently being processed"),
    SUCCESS("SUCCESS", "Transaction has been successfully completed"),
    FAILED("FAILED", "Transaction has failed"),
    CANCELLED("CANCELLED", "Transaction was cancelled"),
    REVERSED("REVERSED", "Transaction was reversed");

    private final String value;
    private final String description;

    TransactionStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public boolean isTerminal() {
        return this == SUCCESS || this == FAILED || this == CANCELLED;
    }

    public boolean isInProgress() {
        return this == PENDING || this == PROCESSING;
    }

    public static Optional<TransactionStatus> fromString(String text) {
        return Arrays.stream(values()).filter(status -> status.name().equalsIgnoreCase(text) ||
                status.getValue().equalsIgnoreCase(text)).findFirst();
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", this.value, this.name());
    }
}