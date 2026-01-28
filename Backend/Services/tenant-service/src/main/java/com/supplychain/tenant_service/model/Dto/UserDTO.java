package com.supplychain.tenant_service.model.Dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.supplychain.tenant_service.model.eNums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private UUID id;
    private UUID tenantId;
    private String email;
    private String fullName;
    private UserRole userRole;
    private Boolean isActive;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;

}
