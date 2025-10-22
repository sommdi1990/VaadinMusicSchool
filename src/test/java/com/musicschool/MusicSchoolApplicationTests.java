package com.musicschool;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Basic integration test for the Music School Management System.
 */
@SpringBootTest
@ActiveProfiles("test")
class MusicSchoolApplicationTests {

    @Test
    void contextLoads() {
        // This test ensures that the Spring context loads successfully
        // If this test passes, it means all beans are properly configured
    }
}
