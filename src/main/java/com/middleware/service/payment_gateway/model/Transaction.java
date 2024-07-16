package com.middleware.service.payment_gateway.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "TBL_TRANSACTION")
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String transRef;
    private String status;
    @NotNull(message = "Transaction amount cannot be empty")
    private BigDecimal amount;
    private String description;
}