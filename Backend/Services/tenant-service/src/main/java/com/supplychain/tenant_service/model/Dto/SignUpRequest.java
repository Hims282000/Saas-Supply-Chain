package com.supplychain.tenant_service.model.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "Tenant name is required")
    @Size(min = 2,max = 255,message = "Tenant must be between 2 nad 255 characters")
    private String tenantName;


    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required ")
    @Size(min = 8, message = "Password must b at least 6 characters")
    private String password;

    @NotBlank(message = "Full name is required")
    private String fullName;

    
}
