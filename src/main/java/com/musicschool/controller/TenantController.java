package com.musicschool.controller;

import com.musicschool.entity.Tenant;
import com.musicschool.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for multi-tenant management endpoints.
 */
@RestController
@RequestMapping("/api/tenants")
@CrossOrigin(origins = "*")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    /**
     * Create a new tenant
     */
    @PostMapping
    public ResponseEntity<Tenant> createTenant(@RequestBody Tenant tenant) {
        Tenant createdTenant = tenantService.createTenant(tenant);
        return ResponseEntity.ok(createdTenant);
    }

    /**
     * Get tenant by domain
     */
    @GetMapping("/domain/{domain}")
    public ResponseEntity<Tenant> getTenantByDomain(@PathVariable String domain) {
        Optional<Tenant> tenant = tenantService.getTenantByDomain(domain);
        return tenant.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get tenant by ID
     */
    @GetMapping("/{tenantId}")
    public ResponseEntity<Tenant> getTenantById(@PathVariable Long tenantId) {
        Optional<Tenant> tenant = tenantService.getTenantById(tenantId);
        return tenant.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update tenant information
     */
    @PutMapping("/{tenantId}")
    public ResponseEntity<Tenant> updateTenant(
            @PathVariable Long tenantId,
            @RequestBody Tenant updatedTenant) {
        
        Tenant tenant = tenantService.updateTenant(tenantId, updatedTenant);
        return ResponseEntity.ok(tenant);
    }

    /**
     * Update tenant subscription
     */
    @PutMapping("/{tenantId}/subscription")
    public ResponseEntity<Tenant> updateSubscription(
            @PathVariable Long tenantId,
            @RequestParam String plan,
            @RequestParam Integer maxStudents,
            @RequestParam Integer maxInstructors,
            @RequestParam Integer maxCourses) {
        
        Tenant tenant = tenantService.updateSubscription(
            tenantId, plan, maxStudents, maxInstructors, maxCourses);
        return ResponseEntity.ok(tenant);
    }

    /**
     * Suspend tenant
     */
    @PostMapping("/{tenantId}/suspend")
    public ResponseEntity<Tenant> suspendTenant(
            @PathVariable Long tenantId,
            @RequestParam String reason) {
        
        Tenant tenant = tenantService.suspendTenant(tenantId, reason);
        return ResponseEntity.ok(tenant);
    }

    /**
     * Activate tenant
     */
    @PostMapping("/{tenantId}/activate")
    public ResponseEntity<Tenant> activateTenant(@PathVariable Long tenantId) {
        Tenant tenant = tenantService.activateTenant(tenantId);
        return ResponseEntity.ok(tenant);
    }

    /**
     * Check if tenant has reached limits
     */
    @GetMapping("/{tenantId}/limits/{limitType}")
    public ResponseEntity<Boolean> hasReachedLimit(
            @PathVariable Long tenantId,
            @PathVariable String limitType) {
        
        boolean hasReachedLimit = tenantService.hasReachedLimit(tenantId, limitType);
        return ResponseEntity.ok(hasReachedLimit);
    }

    /**
     * Get tenant usage statistics
     */
    @GetMapping("/{tenantId}/usage-stats")
    public ResponseEntity<TenantService.TenantUsageStats> getTenantUsageStats(@PathVariable Long tenantId) {
        TenantService.TenantUsageStats stats = tenantService.getTenantUsageStats(tenantId);
        return ResponseEntity.ok(stats);
    }

    /**
     * Get all tenants
     */
    @GetMapping
    public ResponseEntity<List<Tenant>> getAllTenants() {
        List<Tenant> tenants = tenantService.getAllTenants();
        return ResponseEntity.ok(tenants);
    }

    /**
     * Get active tenants
     */
    @GetMapping("/active")
    public ResponseEntity<List<Tenant>> getActiveTenants() {
        List<Tenant> tenants = tenantService.getActiveTenants();
        return ResponseEntity.ok(tenants);
    }

    /**
     * Get tenants by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Tenant>> getTenantsByStatus(@PathVariable String status) {
        List<Tenant> tenants = tenantService.getTenantsByStatus(
            Tenant.TenantStatus.valueOf(status.toUpperCase()));
        return ResponseEntity.ok(tenants);
    }

    /**
     * Check if domain is available
     */
    @GetMapping("/domain/{domain}/available")
    public ResponseEntity<Boolean> isDomainAvailable(@PathVariable String domain) {
        boolean isAvailable = tenantService.isDomainAvailable(domain);
        return ResponseEntity.ok(isAvailable);
    }

    /**
     * Get tenant settings
     */
    @GetMapping("/{tenantId}/settings")
    public ResponseEntity<String> getTenantSettings(@PathVariable Long tenantId) {
        String settings = tenantService.getTenantSettings(tenantId);
        return ResponseEntity.ok(settings);
    }

    /**
     * Update tenant settings
     */
    @PutMapping("/{tenantId}/settings")
    public ResponseEntity<Tenant> updateTenantSettings(
            @PathVariable Long tenantId,
            @RequestBody String settings) {
        
        Tenant tenant = tenantService.updateTenantSettings(tenantId, settings);
        return ResponseEntity.ok(tenant);
    }
}
