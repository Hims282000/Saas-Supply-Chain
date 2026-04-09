package com.supplychain.tenant_service.model.Dto;

import  com.supplychain.tenant_service.model.eNums.SubscriptionTier;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    
    // Tenant Information
    @NotBlank(message = "Tenant name is required")
    private String tenantName;
    
    @NotBlank(message = "Subdomain is required")
    private String subdomain;
    
    @NotNull(message = "Subscription tier is required")
    private SubscriptionTier subscriptionTier;
    
    // User Information (first admin)
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 128, message = "Password must be 8-128 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).*$",
            message = "Password must contain uppercase, lowercase, digit, and special character")
    private String password;
    
    @NotBlank(message = "Full name is required")
    private String fullName;
}
