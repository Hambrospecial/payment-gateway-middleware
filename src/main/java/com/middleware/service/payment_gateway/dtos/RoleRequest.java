package com.middleware.service.payment_gateway.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class RoleRequest {
    @NotBlank(message = "role name cannot be blank")
    @Pattern(regexp = "^[A-Z_\\s-]+$", message = "Role must be in uppercase letters, underscores, or hyphens")
    @Length(min = 3, message = "Role name cannot be less than 3 characters")
    private String name;

    @NotBlank(message = "role description cannot be blank")
    @Pattern(regexp = "^[a-zA-Z0-9._\\s-]{3,20}$", message = "Description can only contain letters, numbers, dots, underscores, hyphens, and spaces")
    private String description;

}
