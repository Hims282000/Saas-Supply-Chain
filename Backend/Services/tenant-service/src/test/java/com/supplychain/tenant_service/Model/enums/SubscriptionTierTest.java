package com.supplychain.tenant_service.Model.enums;


import org.junit.jupiter.api.Test;

import com.supplychain.tenant_service.model.eNums.SubscriptionTier;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionTierTest {

    @Test
    void testEnumValues() {
        // Given & When
        SubscriptionTier[] values = SubscriptionTier.values();
        
        // Then
        assertEquals(4, values.length);
        assertArrayEquals(new SubscriptionTier[]{
            SubscriptionTier.FREE,
            SubscriptionTier.STARTER,
            SubscriptionTier.PROFESSIONAL,
            SubscriptionTier.ENTERPRISE
        }, values);
    }

    @Test
    void testValueOf() {
        // When & Then
        assertEquals(SubscriptionTier.FREE, SubscriptionTier.valueOf("FREE"));
        assertEquals(SubscriptionTier.STARTER, SubscriptionTier.valueOf("STARTER"));
        assertEquals(SubscriptionTier.PROFESSIONAL, SubscriptionTier.valueOf("PROFESSIONAL"));
        assertEquals(SubscriptionTier.ENTERPRISE, SubscriptionTier.valueOf("ENTERPRISE"));
    }

    @Test
    void testEnumProperties() {
        // When & Then
        assertEquals("FREE", SubscriptionTier.FREE.name());
        assertEquals("STARTER", SubscriptionTier.STARTER.name());
        assertEquals(2, SubscriptionTier.PROFESSIONAL.ordinal());
        assertEquals(3, SubscriptionTier.ENTERPRISE.ordinal());
    }
}
