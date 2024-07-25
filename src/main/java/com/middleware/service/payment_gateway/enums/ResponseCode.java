package com.middleware.service.payment_gateway.enums;

import lombok.Getter;

@Getter
public enum ResponseCode {

    SUCCESS("000", "Operation successful"),
    INTERNAL_SERVER_ERROR("500", "An unexpected error occurred"),
    FAILED("400", "Operation failed"),
    CANCELLED("300", "Operation cancelled");

    private final String code;
    private final String description;

    ResponseCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s)", this.code, this.description, this.name());
    }

    public static ResponseCode fromCode(String code) {
        for (ResponseCode responseCode : values()) {
            if (responseCode.getCode().equals(code)) {
                return responseCode;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}