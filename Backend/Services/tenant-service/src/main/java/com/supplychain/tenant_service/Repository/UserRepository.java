package com.supplychain.tenant_service.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.supplychain.tenant_service.model.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User ,UUID> {

    Optional<User> findbyEmail(String email);

    Optional<User> findByTeanantIdAndEmail(UUID tenantId, String email);

    List<User> findByTenantId(UUID tenantId);

    boolean existByTeanantIdAndEmail(UUID tenantId, String email);

    long countByTenantId(UUID tenantId);

} 
