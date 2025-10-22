package com.musicschool.service;

import com.musicschool.entity.Tenant;
import com.musicschool.entity.User;
import com.musicschool.repository.TenantRepository;
import com.musicschool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing multi-tenant functionality.
 */
@Service
@Transactional
public class TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create a new tenant
     */
    public Tenant createTenant(Tenant tenant) {
        // Set default subscription plan
        if (tenant.getSubscriptionPlan() == null) {
            tenant.setSubscriptionPlan("BASIC");
        }
        
        // Set default limits
        if (tenant.getMaxStudents() == null) {
            tenant.setMaxStudents(100);
        }
        if (tenant.getMaxInstructors() == null) {
            tenant.setMaxInstructors(20);
        }
        if (tenant.getMaxCourses() == null) {
            tenant.setMaxCourses(50);
        }
        
        // Set subscription dates
        tenant.setSubscriptionStartDate(LocalDateTime.now());
        tenant.setSubscriptionEndDate(LocalDateTime.now().plusYears(1));
        
        return tenantRepository.save(tenant);
    }

    /**
     * Get tenant by domain
     */
    public Optional<Tenant> getTenantByDomain(String domain) {
        return tenantRepository.findByDomain(domain);
    }

    /**
     * Get tenant by ID
     */
    public Optional<Tenant> getTenantById(Long id) {
        return tenantRepository.findById(id);
    }

    /**
     * Update tenant information
     */
    public Tenant updateTenant(Long tenantId, Tenant updatedTenant) {
        Tenant tenant = tenantRepository.findById(tenantId)
            .orElseThrow(() -> new RuntimeException("Tenant not found"));
        
        tenant.setName(updatedTenant.getName());
        tenant.setDescription(updatedTenant.getDescription());
        tenant.setContactEmail(updatedTenant.getContactEmail());
        tenant.setContactPhone(updatedTenant.getContactPhone());
        tenant.setAddress(updatedTenant.getAddress());
        tenant.setLogoUrl(updatedTenant.getLogoUrl());
        tenant.setWebsite(updatedTenant.getWebsite());
        tenant.setSettings(updatedTenant.getSettings());
        
        return tenantRepository.save(tenant);
    }

    /**
     * Update tenant subscription
     */
    public Tenant updateSubscription(Long tenantId, String plan, Integer maxStudents, 
                                   Integer maxInstructors, Integer maxCourses) {
        Tenant tenant = tenantRepository.findById(tenantId)
            .orElseThrow(() -> new RuntimeException("Tenant not found"));
        
        tenant.setSubscriptionPlan(plan);
        tenant.setMaxStudents(maxStudents);
        tenant.setMaxInstructors(maxInstructors);
        tenant.setMaxCourses(maxCourses);
        
        return tenantRepository.save(tenant);
    }

    /**
     * Suspend tenant
     */
    public Tenant suspendTenant(Long tenantId, String reason) {
        Tenant tenant = tenantRepository.findById(tenantId)
            .orElseThrow(() -> new RuntimeException("Tenant not found"));
        
        tenant.setStatus(Tenant.TenantStatus.SUSPENDED);
        
        // Update tenant settings with suspension reason
        String settings = tenant.getSettings();
        if (settings == null) {
            settings = "{}";
        }
        // Add suspension reason to settings (simplified JSON update)
        settings = settings.replace("}", ",\"suspension_reason\":\"" + reason + "\"}");
        tenant.setSettings(settings);
        
        return tenantRepository.save(tenant);
    }

    /**
     * Activate tenant
     */
    public Tenant activateTenant(Long tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
            .orElseThrow(() -> new RuntimeException("Tenant not found"));
        
        tenant.setStatus(Tenant.TenantStatus.ACTIVE);
        
        // Remove suspension reason from settings
        String settings = tenant.getSettings();
        if (settings != null && settings.contains("suspension_reason")) {
            settings = settings.replaceAll(",\"suspension_reason\":\"[^\"]*\"", "");
            tenant.setSettings(settings);
        }
        
        return tenantRepository.save(tenant);
    }

    /**
     * Check if tenant has reached limits
     */
    public boolean hasReachedLimit(Long tenantId, String limitType) {
        Tenant tenant = tenantRepository.findById(tenantId)
            .orElseThrow(() -> new RuntimeException("Tenant not found"));
        
        switch (limitType.toUpperCase()) {
            case "STUDENTS":
                return tenant.getStudents() != null && 
                       tenant.getStudents().size() >= tenant.getMaxStudents();
            case "INSTRUCTORS":
                return tenant.getInstructors() != null && 
                       tenant.getInstructors().size() >= tenant.getMaxInstructors();
            case "COURSES":
                return tenant.getCourses() != null && 
                       tenant.getCourses().size() >= tenant.getMaxCourses();
            default:
                return false;
        }
    }

    /**
     * Get tenant usage statistics
     */
    public TenantUsageStats getTenantUsageStats(Long tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
            .orElseThrow(() -> new RuntimeException("Tenant not found"));
        
        TenantUsageStats stats = new TenantUsageStats();
        stats.setTenantId(tenantId);
        stats.setTenantName(tenant.getName());
        stats.setCurrentStudents(tenant.getStudents() != null ? tenant.getStudents().size() : 0);
        stats.setMaxStudents(tenant.getMaxStudents());
        stats.setCurrentInstructors(tenant.getInstructors() != null ? tenant.getInstructors().size() : 0);
        stats.setMaxInstructors(tenant.getMaxInstructors());
        stats.setCurrentCourses(tenant.getCourses() != null ? tenant.getCourses().size() : 0);
        stats.setMaxCourses(tenant.getMaxCourses());
        stats.setSubscriptionPlan(tenant.getSubscriptionPlan());
        stats.setStatus(tenant.getStatus());
        
        return stats;
    }

    /**
     * Get all tenants
     */
    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    /**
     * Get active tenants
     */
    public List<Tenant> getActiveTenants() {
        return tenantRepository.findByStatus(Tenant.TenantStatus.ACTIVE);
    }

    /**
     * Get tenants by status
     */
    public List<Tenant> getTenantsByStatus(Tenant.TenantStatus status) {
        return tenantRepository.findByStatus(status);
    }

    /**
     * Check if domain is available
     */
    public boolean isDomainAvailable(String domain) {
        return !tenantRepository.findByDomain(domain).isPresent();
    }

    /**
     * Get tenant settings
     */
    public String getTenantSettings(Long tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
            .orElseThrow(() -> new RuntimeException("Tenant not found"));
        return tenant.getSettings();
    }

    /**
     * Update tenant settings
     */
    public Tenant updateTenantSettings(Long tenantId, String settings) {
        Tenant tenant = tenantRepository.findById(tenantId)
            .orElseThrow(() -> new RuntimeException("Tenant not found"));
        
        tenant.setSettings(settings);
        return tenantRepository.save(tenant);
    }

    /**
     * Get users for a tenant
     */
    public List<User> getTenantUsers(Long tenantId) {
        return userRepository.findByTenantId(tenantId);
    }

    /**
     * Inner class for tenant usage statistics
     */
    public static class TenantUsageStats {
        private Long tenantId;
        private String tenantName;
        private Integer currentStudents;
        private Integer maxStudents;
        private Integer currentInstructors;
        private Integer maxInstructors;
        private Integer currentCourses;
        private Integer maxCourses;
        private String subscriptionPlan;
        private Tenant.TenantStatus status;
        
        // Getters and setters
        public Long getTenantId() { return tenantId; }
        public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
        
        public String getTenantName() { return tenantName; }
        public void setTenantName(String tenantName) { this.tenantName = tenantName; }
        
        public Integer getCurrentStudents() { return currentStudents; }
        public void setCurrentStudents(Integer currentStudents) { this.currentStudents = currentStudents; }
        
        public Integer getMaxStudents() { return maxStudents; }
        public void setMaxStudents(Integer maxStudents) { this.maxStudents = maxStudents; }
        
        public Integer getCurrentInstructors() { return currentInstructors; }
        public void setCurrentInstructors(Integer currentInstructors) { this.currentInstructors = currentInstructors; }
        
        public Integer getMaxInstructors() { return maxInstructors; }
        public void setMaxInstructors(Integer maxInstructors) { this.maxInstructors = maxInstructors; }
        
        public Integer getCurrentCourses() { return currentCourses; }
        public void setCurrentCourses(Integer currentCourses) { this.currentCourses = currentCourses; }
        
        public Integer getMaxCourses() { return maxCourses; }
        public void setMaxCourses(Integer maxCourses) { this.maxCourses = maxCourses; }
        
        public String getSubscriptionPlan() { return subscriptionPlan; }
        public void setSubscriptionPlan(String subscriptionPlan) { this.subscriptionPlan = subscriptionPlan; }
        
        public Tenant.TenantStatus getStatus() { return status; }
        public void setStatus(Tenant.TenantStatus status) { this.status = status; }
    }
}
