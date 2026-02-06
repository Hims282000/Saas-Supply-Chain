package com.supplychain.tenant_service.Model.enums;


import org.junit.jupiter.api.Test;

import com.supplychain.tenant_service.model.eNums.UserRole;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

class UserRoleTest {

    @Test
    void testEnumValues() {
        // Given & When
        UserRole[] values = UserRole.values();
        
        // Then
        assertEquals(5, values.length);
        assertArrayEquals(new UserRole[]{
            UserRole.SUPER_ADMIN,
            UserRole.ADMIN,
            UserRole.MANAGER,
            UserRole.VIEWER,
            UserRole.ANALYST
        }, values);
    }

    @Test
    void testRoleHierarchy() {
        // Given
        List<UserRole> expectedOrder = Arrays.asList(
            UserRole.SUPER_ADMIN,  // Highest
            UserRole.ADMIN,
            UserRole.MANAGER,
            UserRole.ANALYST,
            UserRole.VIEWER        // Lowest
        );
        
        // When & Then
        for (int i = 0; i < expectedOrder.size() - 1; i++) {
            UserRole higher = expectedOrder.get(i);
            UserRole lower = expectedOrder.get(i + 1);
            assertTrue(higher.ordinal() < lower.ordinal());
        }
    }

    @Test
    void testSuperAdminIsHighest() {
        assertEquals(0, UserRole.SUPER_ADMIN.ordinal());
        assertEquals("SUPER_ADMIN", UserRole.SUPER_ADMIN.name());
    }

    @Test
    void testViewerIsLowest() {
        assertEquals(3, UserRole.VIEWER.ordinal()); // Based on enum order
        assertEquals("VIEWER", UserRole.VIEWER.name());
    }
}