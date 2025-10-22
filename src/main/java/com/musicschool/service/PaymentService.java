package com.musicschool.service;

import com.musicschool.entity.Payment;
import com.musicschool.entity.Student;
import com.musicschool.entity.Course;
import com.musicschool.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for handling payment processing with Stripe integration.
 */
@Service
@Transactional
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @Value("${stripe.publishable-key}")
    private String stripePublishableKey;

    /**
     * Create a payment intent for course fees
     */
    public Map<String, Object> createPaymentIntent(BigDecimal amount, String currency, 
                                                  Student student, Course course, String description) {
        Stripe.apiKey = stripeSecretKey;

        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount.multiply(new BigDecimal("100")).longValue()) // Convert to cents
                .setCurrency(currency.toLowerCase())
                .setDescription(description)
                .putMetadata("student_id", student.getId().toString())
                .putMetadata("course_id", course.getId().toString())
                .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Save payment record
            Payment payment = new Payment(amount, Payment.PaymentType.COURSE_FEE, student, course);
            payment.setStripePaymentIntentId(paymentIntent.getId());
            payment.setDescription(description);
            payment.setStatus(Payment.PaymentStatus.PENDING);
            paymentRepository.save(payment);

            Map<String, Object> response = new HashMap<>();
            response.put("client_secret", paymentIntent.getClientSecret());
            response.put("payment_id", payment.getId());
            response.put("amount", amount);
            response.put("currency", currency);

            return response;

        } catch (StripeException e) {
            throw new RuntimeException("Failed to create payment intent: " + e.getMessage(), e);
        }
    }

    /**
     * Process payment completion
     */
    public Payment processPaymentCompletion(String paymentIntentId) {
        Stripe.apiKey = stripeSecretKey;

        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            
            List<Payment> payments = paymentRepository.findByStripePaymentIntentId(paymentIntentId);
            if (payments.isEmpty()) {
                throw new RuntimeException("Payment not found");
            }
            Payment payment = payments.get(0);

            if (paymentIntent.getStatus().equals("succeeded")) {
                payment.setStatus(Payment.PaymentStatus.COMPLETED);
                payment.setProcessedAt(LocalDateTime.now());
                payment.setTransactionId(paymentIntent.getLatestCharge());
                payment.setPaymentMethod("card");
            } else if (paymentIntent.getStatus().equals("requires_payment_method")) {
                payment.setStatus(Payment.PaymentStatus.FAILED);
                payment.setFailureReason("Payment method required");
            } else {
                payment.setStatus(Payment.PaymentStatus.FAILED);
                payment.setFailureReason("Payment failed");
            }

            return paymentRepository.save(payment);

        } catch (StripeException e) {
            throw new RuntimeException("Failed to process payment: " + e.getMessage(), e);
        }
    }

    /**
     * Process refund
     */
    public Payment processRefund(Long paymentId, BigDecimal refundAmount, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != Payment.PaymentStatus.COMPLETED) {
            throw new RuntimeException("Cannot refund payment that is not completed");
        }

        Stripe.apiKey = stripeSecretKey;

        try {
            // Create refund in Stripe
            com.stripe.model.Refund refund = com.stripe.model.Refund.create(
                com.stripe.param.RefundCreateParams.builder()
                    .setCharge(payment.getStripeChargeId())
                    .setAmount(refundAmount.multiply(new BigDecimal("100")).longValue())
                    .setReason(com.stripe.param.RefundCreateParams.Reason.REQUESTED_BY_CUSTOMER)
                    .putMetadata("reason", reason)
                    .build()
            );

            // Update payment record
            if (refundAmount.compareTo(payment.getAmount()) == 0) {
                payment.setStatus(Payment.PaymentStatus.REFUNDED);
            } else {
                payment.setStatus(Payment.PaymentStatus.PARTIALLY_REFUNDED);
            }
            payment.setRefundAmount(refundAmount);
            payment.setRefundReason(reason);

            return paymentRepository.save(payment);

        } catch (StripeException e) {
            throw new RuntimeException("Failed to process refund: " + e.getMessage(), e);
        }
    }

    /**
     * Get payment status
     */
    public Payment.PaymentStatus getPaymentStatus(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
        return payment.getStatus();
    }

    /**
     * Get payment history for a student
     */
    public java.util.List<Payment> getPaymentHistoryForStudent(Long studentId) {
        return paymentRepository.findByStudentIdOrderByCreatedAtDesc(studentId);
    }

    /**
     * Get payment history for a course
     */
    public java.util.List<Payment> getPaymentHistoryForCourse(Long courseId) {
        return paymentRepository.findByCourseIdOrderByCreatedAtDesc(courseId);
    }

    /**
     * Calculate total revenue for a period
     */
    public BigDecimal calculateTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findByProcessedAtBetween(startDate, endDate)
            .stream()
            .filter(p -> p.getStatus() == Payment.PaymentStatus.COMPLETED)
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculate total refunds for a period
     */
    public BigDecimal calculateTotalRefunds(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findByProcessedAtBetween(startDate, endDate)
            .stream()
            .filter(p -> p.getStatus() == Payment.PaymentStatus.REFUNDED || 
                        p.getStatus() == Payment.PaymentStatus.PARTIALLY_REFUNDED)
            .map(p -> p.getRefundAmount() != null ? p.getRefundAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
