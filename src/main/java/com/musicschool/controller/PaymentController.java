package com.musicschool.controller;

import com.musicschool.entity.Payment;
import com.musicschool.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for payment processing endpoints.
 */
@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * Create a payment intent for course fees
     */
    @PostMapping("/create-intent")
    public ResponseEntity<Map<String, Object>> createPaymentIntent(
            @RequestParam BigDecimal amount,
            @RequestParam String currency,
            @RequestParam Long studentId,
            @RequestParam Long courseId,
            @RequestParam String description) {
        
        // Get student and course entities (simplified for demo)
        // In real implementation, you would fetch these from repositories
        Map<String, Object> response = paymentService.createPaymentIntent(
            amount, currency, null, null, description);
        return ResponseEntity.ok(response);
    }

    /**
     * Process payment completion
     */
    @PostMapping("/complete")
    public ResponseEntity<Payment> completePayment(@RequestParam String paymentIntentId) {
        Payment payment = paymentService.processPaymentCompletion(paymentIntentId);
        return ResponseEntity.ok(payment);
    }

    /**
     * Process refund
     */
    @PostMapping("/refund")
    public ResponseEntity<Payment> processRefund(
            @RequestParam Long paymentId,
            @RequestParam BigDecimal refundAmount,
            @RequestParam String reason) {
        
        Payment payment = paymentService.processRefund(paymentId, refundAmount, reason);
        return ResponseEntity.ok(payment);
    }

    /**
     * Get payment status
     */
    @GetMapping("/{paymentId}/status")
    public ResponseEntity<Payment.PaymentStatus> getPaymentStatus(@PathVariable Long paymentId) {
        Payment.PaymentStatus status = paymentService.getPaymentStatus(paymentId);
        return ResponseEntity.ok(status);
    }

    /**
     * Get payment history for a student
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Payment>> getStudentPaymentHistory(@PathVariable Long studentId) {
        List<Payment> payments = paymentService.getPaymentHistoryForStudent(studentId);
        return ResponseEntity.ok(payments);
    }

    /**
     * Get payment history for a course
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Payment>> getCoursePaymentHistory(@PathVariable Long courseId) {
        List<Payment> payments = paymentService.getPaymentHistoryForCourse(courseId);
        return ResponseEntity.ok(payments);
    }

    /**
     * Calculate total revenue for a period
     */
    @GetMapping("/revenue")
    public ResponseEntity<BigDecimal> calculateTotalRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        BigDecimal revenue = paymentService.calculateTotalRevenue(startDate, endDate);
        return ResponseEntity.ok(revenue);
    }

    /**
     * Calculate total refunds for a period
     */
    @GetMapping("/refunds")
    public ResponseEntity<BigDecimal> calculateTotalRefunds(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        BigDecimal refunds = paymentService.calculateTotalRefunds(startDate, endDate);
        return ResponseEntity.ok(refunds);
    }
}
