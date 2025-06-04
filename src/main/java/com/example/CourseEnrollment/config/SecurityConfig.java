package com.example.CourseEnrollment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/students", "/courses", "/enrollemnts", "/edit_enrollpage").permitAll() // Allow public access
                .requestMatchers("/api/**").authenticated() // Protect APIs
                .anyRequest().authenticated() // Everything else requires login
            )
            .httpBasic(Customizer.withDefaults()); 
        return http.build();
    }
}

// .requestMatchers("/api/**").permitAll() // Allow all REST endpoints
//                 .anyRequest().authenticated()
