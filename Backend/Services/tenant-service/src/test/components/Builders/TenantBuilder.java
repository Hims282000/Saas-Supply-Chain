package com.supplychain.tenant.components.builders;

import com.supplychain.tenant.model.entity.Tenant;
import com.supplychain.tenant.model.enums.SubscriptionTier;
import com.supplychain.tenant.model.enums.TenantStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class TenantBuilder {
    
    private UUID id;
    private String name;
    private String subdomain;
    private SubscriptionTier subscriptionTier = SubscriptionTier.FREE;
    private TenantStatus status = TenantStatus.ACTIVE;
    private Tenant.TenantConfig config;
    private LocalDateTime createdAt = LocalDateTime.now();
    
    public TenantBuilder withId(UUID id) {
        this.id = id;
        return this;
    }
    
    public TenantBuilder withName(String name) {
        this.name = name;
        return this;
    }
    
    public TenantBuilder withSubdomain(String subdomain) {
        this.subdomain = subdomain;
        return this;
    }
    
    public TenantBuilder withSubscriptionTier(SubscriptionTier tier) {
        this.subscriptionTier = tier;
        return this;
    }
    
    public TenantBuilder withStatus(TenantStatus status) {
        this.status = status;
        return this;
    }
    
    public TenantBuilder withConfig(Tenant.TenantConfig config) {
        this.config = config;
        return this;
    }
    
    public TenantBuilder withDefaultConfig() {
        this.config = Tenant.TenantConfig.builder()
                .timezone("UTC")
                .currency("USD")
                .emailNotifications(true)
                .smsNotifications(false)
                .maxUsers(10)
                .maxShipmentsPerMonth(1000)
                .build();
        return this;
    }
    
    public Tenant build() {
        Tenant tenant = Tenant.builder()
                .id(id)
                .name(name)
                .subdomain(subdomain)
                .subscriptionTier(subscriptionTier)
                .status(status)
                .config(config)
                .createdAt(createdAt)
                .build();
        
        return tenant;
    }
    
    public static TenantBuilder aTenant() {
        return new TenantBuilder();
    }
    
    public static Tenant defaultTenant() {
        return aTenant()
                .withId(UUID.randomUUID())
                .withName("Default Test Company")
                .withSubdomain("default-test")
                .withSubscriptionTier(SubscriptionTier.STARTER)
                .withDefaultConfig()
                .build();
    }
}