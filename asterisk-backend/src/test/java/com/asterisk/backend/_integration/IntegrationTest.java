package com.asterisk.backend._integration;

import com.asterisk.backend.application.config.TokenProperties;
import com.asterisk.backend.application.security.jwt.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@EnableConfigurationProperties(value = {JwtProperties.class, TokenProperties.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@Testcontainers
@Transactional
public abstract class IntegrationTest {

    @Container
    public static AsteriskPostgresContainer postgreSQLContainer = AsteriskPostgresContainer.getInstance();

    public void setUp() {

    }
}
