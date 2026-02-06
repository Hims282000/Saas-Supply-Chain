package com.supplychain.tenant_service.model.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Email is required ")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is Required")
    private String password;
    
}
