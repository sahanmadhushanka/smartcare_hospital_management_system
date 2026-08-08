package com.hospitalmanagement.smartcare.service;

import com.hospitalmanagement.smartcare.entity.User;
import com.hospitalmanagement.smartcare.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Register a new user
    public User register(User user) {

        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // Find user by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Login
    public boolean login(String username, String password) {

        Optional<User> userOptional =
                userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();

        return passwordEncoder.matches(
                password,
                user.getPassword()
        );
    }
    // Reset password
    public boolean resetPassword(String username, String newPassword) {

        Optional<User> userOptional =
                userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();

        // Hash the new password before saving
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        return true;
    }
}