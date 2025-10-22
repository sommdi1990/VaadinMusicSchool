package com.musicschool.repository;

import com.musicschool.entity.User;
import com.musicschool.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    List<User> findByStatus(User.UserStatus status);
    
    List<User> findByTenant(Tenant tenant);
    
    List<User> findByTenantId(Long tenantId);
    
    List<User> findByRolesContaining(User.Role role);
    
    @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :username")
    Optional<User> findByUsernameOrEmail(@Param("username") String username);
    
    @Query("SELECT u FROM User u WHERE u.status = :status AND u.lastLogin < :cutoffDate")
    List<User> findInactiveUsers(@Param("status") User.UserStatus status, 
                               @Param("cutoffDate") LocalDateTime cutoffDate);
    
    @Query("SELECT u FROM User u WHERE u.lockedUntil IS NOT NULL AND u.lockedUntil > :currentTime")
    List<User> findLockedUsers(@Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT u FROM User u WHERE u.emailVerified = false")
    List<User> findUnverifiedUsers();
    
    @Query("SELECT u FROM User u WHERE u.passwordResetToken = :token AND u.passwordResetExpires > :currentTime")
    Optional<User> findByValidPasswordResetToken(@Param("token") String token, 
                                               @Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT u FROM User u WHERE u.emailVerificationToken = :token")
    Optional<User> findByEmailVerificationToken(@Param("token") String token);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.status = :status")
    Long countByStatus(@Param("status") User.UserStatus status);
    
    @Query("SELECT u.roles, COUNT(u) FROM User u GROUP BY u.roles")
    List<Object[]> getUserCountByRole();
}
