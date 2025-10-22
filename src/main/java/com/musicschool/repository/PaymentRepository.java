package com.musicschool.repository;

import com.musicschool.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Payment entity.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    List<Payment> findByStatus(Payment.PaymentStatus status);
    
    List<Payment> findByPaymentType(Payment.PaymentType paymentType);
    
    List<Payment> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    
    List<Payment> findByCourseIdOrderByCreatedAtDesc(Long courseId);
    
    List<Payment> findByProcessedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Payment> findByStripePaymentIntentId(String paymentIntentId);
    
    List<Payment> findByStripeChargeId(String chargeId);
    
    @Query("SELECT p FROM Payment p WHERE p.student.id = :studentId AND p.status = :status")
    List<Payment> findByStudentAndStatus(@Param("studentId") Long studentId, 
                                        @Param("status") Payment.PaymentStatus status);
    
    @Query("SELECT p FROM Payment p WHERE p.course.id = :courseId AND p.status = :status")
    List<Payment> findByCourseAndStatus(@Param("courseId") Long courseId, 
                                       @Param("status") Payment.PaymentStatus status);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = :status AND p.processedAt BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalRevenue(@Param("status") Payment.PaymentStatus status, 
                                   @Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = :status AND p.processedAt BETWEEN :startDate AND :endDate")
    Long countPaymentsByStatusAndDateRange(@Param("status") Payment.PaymentStatus status, 
                                         @Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT p.paymentType, SUM(p.amount) FROM Payment p WHERE p.status = :status AND p.processedAt BETWEEN :startDate AND :endDate GROUP BY p.paymentType")
    List<Object[]> getRevenueByPaymentType(@Param("status") Payment.PaymentStatus status, 
                                          @Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
}
