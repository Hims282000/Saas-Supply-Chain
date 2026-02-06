package com.supplychain.tenant_service.Model.entity;


import com.supplychain.tenant_service.model.eNums.SubscriptionTier;
import com.supplychain.tenant_service.model.eNums.SubscriptionStatus;
import com.supplychain.tenant_service.model.Entity.Tenant;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class TenantTest {

    private Tenant tenant;
    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        
        tenant = Tenant.builder()
                .id(tenantId)
                .name("Acme Corporation")
                .subdomain("acme")
                .subscriptionTier(SubscriptionTier.PROFESSIONAL)
                .status(SubscriptionStatus.ACTIVE)
                .config(Tenant.TenantConfig.builder()
                        .timezone("America/New_York")
                        .currency("USD")
                        .emailNotifications(true)
                        .smsNotifications(false)
                        .maxUsers(50)
                        .maxShipmentsPerMonth(10000)
                        .build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testTenantCreation() {
        // Then
        assertEquals(tenantId, tenant.getId());
        assertEquals("Acme Corporation", tenant.getName());
        assertEquals("acme", tenant.getSubdomain());
        assertEquals(SubscriptionTier.PROFESSIONAL, tenant.getSubscriptionTier());
        assertEquals(SubscriptionStatus.ACTIVE, tenant.getStatus());
        assertNotNull(tenant.getConfig());
        assertNotNull(tenant.getCreatedAt());
        assertNotNull(tenant.getUpdatedAt());
    }

    @Test
    void testTenantConfig() {
        // When
        Tenant.TenantConfig config = tenant.getConfig();
        
        // Then
        assertNotNull(config);
        assertEquals("America/New_York", config.getTimezone());
        assertEquals("USD", config.getCurrency());
        assertTrue(config.getEmailNotifications());
        assertFalse(config.getSmsNotifications());
        assertEquals(50, config.getMaxUsers());
        assertEquals(10000, config.getMaxShipmentsPerMonth());
    }

    @Test
    void testTenantConfigBuilder() {
        // Given
        Tenant.TenantConfig config = Tenant.TenantConfig.builder()
                .timezone("UTC")
                .currency("EUR")
                .emailNotifications(false)
                .smsNotifications(true)
                .maxUsers(100)
                .maxShipmentsPerMonth(50000)
                .build();
        
        // When
        tenant.setConfig(config);
        
        // Then
        assertEquals("UTC", tenant.getConfig().getTimezone());
        assertEquals("EUR", tenant.getConfig().getCurrency());
        assertFalse(tenant.getConfig().getEmailNotifications());
        assertTrue(tenant.getConfig().getSmsNotifications());
        assertEquals(100, tenant.getConfig().getMaxUsers());
        assertEquals(50000, tenant.getConfig().getMaxShipmentsPerMonth());
    }

    @Test
    void testTenantEquality() {
        // Given
        Tenant sameTenant = Tenant.builder()
                .id(tenantId)
                .name("Different Name")
                .subdomain("different")
                .build();
        
        Tenant differentTenant = Tenant.builder()
                .id(UUID.randomUUID())
                .name("Acme Corporation")
                .subdomain("acme")
                .build();
        
        // Then
        assertEquals(tenant.getId(), sameTenant.getId());
        assertNotEquals(tenant.getId(), differentTenant.getId());
    }

    @Test
    void testTenantToString() {
        // When
        String toString = tenant.toString();
        
        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("Acme Corporation"));
        assertTrue(toString.contains("acme"));
    }

    @Test
    void testTenantWithNoConfig() {
        // Given
        Tenant noConfigTenant = Tenant.builder()
                .id(UUID.randomUUID())
                .name("No Config Corp")
                .subdomain("noconfig")
                .build();
        
        // Then
        assertNull(noConfigTenant.getConfig());
    }
}
