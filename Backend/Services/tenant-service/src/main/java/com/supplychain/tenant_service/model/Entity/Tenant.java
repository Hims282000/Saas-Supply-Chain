package com.supplychain.tenant_service.model.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.databind.annotation.EnumNaming;
import com.supplychain.tenant_service.model.eNums.SubscriptionStatus;
import com.supplychain.tenant_service.model.eNums.SubscriptionTier;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name= "tenants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false , length = 100)
    private String subdomain;


    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_tier" , nullable = false)
    @Builder.Default
    private SubscriptionTier subscriptionTier;



    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_Sattus", nullable = false)
    @Builder.Default
    private SubscriptionStatus subscriptionStatus;


    @Column(name = "Sripe_customer__id" ,length = 100 )
    private String stripeCustomerId;

    @Column(name = "max_users" )
    @Builder.Default
    private Integer maxUsers=5;


    @Column(name = "max_shipments_per_month" )
    @Builder.Default
    private Integer maxShipmentsPerMonth;

    @Column(name = "max_ai_queries_per_day")
    @Builder.Default
    private Integer maxAiQueriesPerDay;

    @CreationTimestamp
    @Column(name = "created_At" , nullable = false, updatable = false)
    private LocalDate createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at" , nullable = false)
    private LocalDateTime updatedAt;

    
}
