package com.hospitalmanagement.smartcare.controller;

import com.hospitalmanagement.smartcare.entity.User;
import com.hospitalmanagement.smartcare.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Register
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {

        User savedUser = userService.register(user);

        return ResponseEntity.ok(savedUser);
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        boolean success = userService.login(
                user.getUsername(),
                user.getPassword()
        );

        if (!success) {
            return ResponseEntity
                    .status(401)
                    .body("Invalid username or password");
        }

        User loggedUser = userService
                .findByUsername(user.getUsername())
                .orElseThrow();

        return ResponseEntity.ok(
                Map.of(
                        "message", "Login successful",
                        "username", loggedUser.getUsername(),
                        "role", loggedUser.getRole()
                )
        );
    }
    // Reset Password
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody Map<String, String> request) {

        String username = request.get("username");
        String newPassword = request.get("newPassword");

        boolean success =
                userService.resetPassword(username, newPassword);

        if (!success) {
            return ResponseEntity
                    .status(404)
                    .body("User not found");
        }

        return ResponseEntity.ok("Password reset successful");
    }
}