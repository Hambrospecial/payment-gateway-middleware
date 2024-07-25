package com.middleware.service.payment_gateway.dtos;

import com.middleware.service.payment_gateway.enums.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private ResponseCode responseCode;
}
