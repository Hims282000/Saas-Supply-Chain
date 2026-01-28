package com.supplychain.tenant_service.model.Dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.supplychain.tenant_service.model.eNums.SubscriptionStatus;
import com.supplychain.tenant_service.model.eNums.SubscriptionTier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantDTO {

    private UUID id;
    private String name;
    private String subdomain;
    private SubscriptionTier subscriptionTier;
    private SubscriptionStatus subscriptionStatus;
    private Integer maxUsers;
    private Integer maxShipmentsPerMonth;
    private Integer maxAiQueriesPerDay;
    private LocalDateTime createdAt;

}
