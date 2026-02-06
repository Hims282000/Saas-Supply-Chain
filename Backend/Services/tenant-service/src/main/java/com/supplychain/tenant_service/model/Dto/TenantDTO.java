package com.supplychain.tenant_service.model.Dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import com.supplychain.tenant_service.model.eNums.SubscriptionStatus;
import com.supplychain.tenant_service.model.eNums.SubscriptionTier;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantDTO {
    private UUID id;
    private String name;
    private String subdomain;
    private SubscriptionTier subscriptionTier;
    private SubscriptionStatus status;
    private TenantConfigDTO config;
    private LocalDateTime createdAt;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TenantConfigDTO {
        private String timezone;
        private String currency;
        private Boolean emailNotifications;
        private Boolean smsNotifications;
        private Integer maxUsers;
        private Integer maxShipmentsPerMonth;
    }
}