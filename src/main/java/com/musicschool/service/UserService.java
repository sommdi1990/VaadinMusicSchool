package com.musicschool.service;

import com.musicschool.entity.User;
import com.musicschool.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Service class for User entity operations including authentication and registration.
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    /**
     * Register a new user
     */
    public User registerUser(String username, String email, String password,
                             String firstName, String lastName, String phone) {
        // Check if username already exists
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setStatus(User.UserStatus.ACTIVE);
        user.setEmailVerified(false);
        user.setLoginAttempts(0);

        // Set default role
        Set<User.Role> roles = new HashSet<>();
        roles.add(User.Role.STUDENT);
        user.setRoles(roles);

        // Generate email verification token
        user.setEmailVerificationToken(UUID.randomUUID().toString());

        // Save user
        User savedUser = userRepository.save(user);

        // Send welcome email
        try {
            emailService.sendWelcomeEmail(email, user.getFullName());
        } catch (Exception e) {
            // Log error but don't fail registration
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }

        return savedUser;
    }

    /**
     * Authenticate user
     */
    public Optional<User> authenticate(String usernameOrEmail, String password) {
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(usernameOrEmail);

        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();

        // Check if account is locked
        if (!user.isAccountNonLocked()) {
            throw new RuntimeException("Account is locked until " + user.getLockedUntil());
        }

        // Check if account is active
        if (user.getStatus() != User.UserStatus.ACTIVE) {
            throw new RuntimeException("Account is not active");
        }

        // Verify password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            // Increment login attempts
            user.setLoginAttempts(user.getLoginAttempts() + 1);

            // Lock account after 5 failed attempts
            if (user.getLoginAttempts() >= 5) {
                user.setLockedUntil(LocalDateTime.now().plusHours(1));
                user.setStatus(User.UserStatus.LOCKED);
            }

            userRepository.save(user);
            return Optional.empty();
        }

        // Successful login - reset login attempts and update last login
        user.setLoginAttempts(0);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return Optional.of(user);
    }

    /**
     * Find user by ID
     */
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Find user by username
     */
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Find user by email
     */
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Find all users
     */
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Update user
     */
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Delete user
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Change password
     */
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Request password reset
     */
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Generate reset token
        user.setPasswordResetToken(UUID.randomUUID().toString());
        user.setPasswordResetExpires(LocalDateTime.now().plusHours(24));
        userRepository.save(user);

        // Send password reset email
        try {
            String resetLink = "http://localhost:8080/music-school/reset-password?token=" + user.getPasswordResetToken();
            emailService.sendSimpleEmail(
                    email,
                    "Password Reset Request",
                    "Click the following link to reset your password: " + resetLink +
                            "\n\nThis link will expire in 24 hours."
            );
        } catch (Exception e) {
            System.err.println("Failed to send password reset email: " + e.getMessage());
        }
    }

    /**
     * Reset password with token
     */
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByValidPasswordResetToken(token, LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpires(null);
        userRepository.save(user);
    }

    /**
     * Verify email
     */
    public void verifyEmail(String token) {
        User user = userRepository.findAll().stream()
                .filter(u -> token.equals(u.getEmailVerificationToken()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        userRepository.save(user);
    }

    /**
     * Add role to user
     */
    public void addRole(Long userId, User.Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(role);
        userRepository.save(user);
    }

    /**
     * Remove role from user
     */
    public void removeRole(Long userId, User.Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRoles() != null) {
            user.getRoles().remove(role);
            userRepository.save(user);
        }
    }
}

