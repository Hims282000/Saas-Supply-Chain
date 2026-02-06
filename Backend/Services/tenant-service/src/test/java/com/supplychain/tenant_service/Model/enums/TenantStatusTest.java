package com.supplychain.tenant_service.Model.enums;


import org.junit.jupiter.api.Test;

import com.supplychain.tenant_service.model.eNums.SubscriptionStatus;

import static org.junit.jupiter.api.Assertions.*;

class TenantStatusTest {

    @Test
    void testAllStatusValues() {
        // Given & When
        SubscriptionStatus[] values = SubscriptionStatus.values();
        
        // Then
        assertEquals(4, values.length);
        assertEquals(SubscriptionStatus.ACTIVE, SubscriptionStatus.valueOf("ACTIVE"));
        assertEquals(SubscriptionStatus.SUSPENDED, SubscriptionStatus.valueOf("SUSPENDED"));
        assertEquals(SubscriptionStatus.CANCELLED, SubscriptionStatus.valueOf("CANCELLED"));
        assertEquals(SubscriptionStatus.TRIAL, SubscriptionStatus.valueOf("TRIAL"));
    }

    @Test
    void testActiveIsDefault() {
        // Given
        SubscriptionStatus status = SubscriptionStatus.ACTIVE;
        
        // When & Then
        assertEquals("ACTIVE", status.name());
        assertTrue(status.name().equals("ACTIVE"));
    }
}