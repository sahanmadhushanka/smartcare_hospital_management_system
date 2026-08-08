package com.hospitalmanagement.smartcare;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // Register + Login - Public
                        .requestMatchers("/api/auth/**").permitAll()

                        // Admin - ADMIN only
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Doctor - DOCTOR only
                        .requestMatchers("/api/doctor/**").hasRole("DOCTOR")

                        // Patient - PATIENT only
                        .requestMatchers("/api/patient/**").hasRole("PATIENT")

                        // Receptionist - RECEPTIONIST only
                        .requestMatchers("/api/receptionist/**").hasRole("RECEPTIONIST")

                        // Everything else requires login
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form.disable())

                .httpBasic(httpBasic -> {});

        return http.build();
    }
}