package com.musicschool.repository;

import com.musicschool.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Tenant entity.
 */
@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    
    Optional<Tenant> findByDomain(String domain);
    
    List<Tenant> findByStatus(Tenant.TenantStatus status);
    
    List<Tenant> findBySubscriptionPlan(String subscriptionPlan);
    
    @Query("SELECT t FROM Tenant t WHERE t.status = :status AND t.subscriptionEndDate < :currentDate")
    List<Tenant> findExpiredTenants(@Param("status") Tenant.TenantStatus status, 
                                   @Param("currentDate") java.time.LocalDateTime currentDate);
    
    @Query("SELECT t FROM Tenant t WHERE t.name LIKE %:name%")
    List<Tenant> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT t FROM Tenant t WHERE t.contactEmail = :email")
    Optional<Tenant> findByContactEmail(@Param("email") String email);
    
    @Query("SELECT COUNT(t) FROM Tenant t WHERE t.status = :status")
    Long countByStatus(@Param("status") Tenant.TenantStatus status);
    
    @Query("SELECT t.subscriptionPlan, COUNT(t) FROM Tenant t GROUP BY t.subscriptionPlan")
    List<Object[]> getTenantCountBySubscriptionPlan();
}
