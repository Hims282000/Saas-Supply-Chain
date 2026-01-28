package com.supplychain.tenant_service.model.Dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private String tokenType;
    private UUID tenantId;
    private UUID userId;
    private String email;
    private String fullName;
    private String role;

    @Builder.Default
    private String tokenTypeValue= "Bearer";

    
}
