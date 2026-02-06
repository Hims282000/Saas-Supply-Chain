package com.supplychain.tenant_service.model.Dto;

import com.supplychain.tenant_service.model.eNums.SubscriptionTier;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private String password;
    
    @NotBlank(message = "Full name is required")
    private String fullName;
}
