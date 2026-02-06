package com.supplychain.tenant_service.service.event;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.supplychain.tenant_service.model.eNums.SubscriptionTier;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TenantEventPublisher {
    
    private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;
    
    @Value("${app.kafka.topics.tenant-events}")
    private String tenantEventsTopic;
    
    public void publishTenantCreated(UUID tenantId, String name, String subdomain, SubscriptionTier tier) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "TENANT_CREATED");
        event.put("tenantId", tenantId.toString());
        event.put("name", name);
        event.put("subdomain", subdomain);
        event.put("subscriptionTier", tier.name());
        kafkaTemplate.send(tenantEventsTopic, tenantId.toString(), event);
    }
    
    public void publishUserRegistered(UUID tenantId, UUID userId, String email, String role) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "USER_REGISTERED");
        event.put("tenantId", tenantId.toString());
        event.put("userId", userId.toString());
        event.put("email", email);
        event.put("role", role);
        kafkaTemplate.send(tenantEventsTopic, tenantId.toString(), event);
    }
}
