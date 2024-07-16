package com.middleware.service.payment_gateway.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WebhookNotification {
    @NotBlank(message = "Transaction Reference cannot be null or empty")
    private String transRef;
    @NotBlank(message = "Transaction Reference cannot be null or empty")
    private String status;
}

