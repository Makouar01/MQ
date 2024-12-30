package com.ayoub.project_management.service.project.controllerTest;


import com.ayoub.project_management.config.AppConfig;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppConfigTest {

    private final AppConfig appConfig = new AppConfig();


    @Test
    void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = appConfig.passwordEncoder();
        assertNotNull(passwordEncoder, "PasswordEncoder should not be null");
    }

    @Test
    void testCorsConfigurationSource() {
        CorsConfigurationSource corsConfigurationSource = appConfig.corsConfigurationSource();
        CorsConfiguration corsConfiguration = corsConfigurationSource.getCorsConfiguration(null);
        assertNotNull(corsConfiguration, "CorsConfiguration should not be null");
    }
}