package com.ayoub.project_management.service.project;


import com.ayoub.project_management.config.JwtConstant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class JwtConstantTest {
    @Test
    void testConstants() {
        // Assert that the constants have the expected values
        assertEquals("jwtSecret", JwtConstant.SECRET_KEY, "SECRET_KEY should be 'jwtSecret'");
        assertEquals("Authorization", JwtConstant.JWT_HEADER, "JWT_HEADER should be 'Authorization'");
    }
}
