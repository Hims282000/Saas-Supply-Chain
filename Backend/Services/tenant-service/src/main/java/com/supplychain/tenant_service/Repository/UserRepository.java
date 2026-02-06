package com.supplychain.tenant_service.Repository;

import com.supplychain.tenant_service.model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndTenant_Id(String email, UUID tenantId);
    boolean existsByEmailAndTenant_Id(String email, UUID tenantId);
}
