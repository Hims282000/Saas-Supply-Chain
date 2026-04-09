package com.supplychain.tenant_service.Repository;

import com.supplychain.tenant_service.model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndTenant_Id(String email, UUID tenantId);
    boolean existsByEmailAndTenant_Id(String email, UUID tenantId);
    List<User> findByTenant_Id(UUID tenantId);
    
    @Query("SELECT u FROM User u JOIN FETCH u.tenant WHERE u.email = :email")
    Optional<User> findByEmailWithTenant(@Param("email") String email);
}
