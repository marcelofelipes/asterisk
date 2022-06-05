package com.asterisk.backend.application.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "asterisk.jwt")
public class JwtProperties {

    private String accessSecret;
    private Long accessExpiration;

    public JwtProperties() {
    }

    public String getAccessSecret() {
        return this.accessSecret;
    }

    public void setAccessSecret(final String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public Long getAccessExpiration() {
        return this.accessExpiration;
    }

    public void setAccessExpiration(final Long accessExpiration) {
        this.accessExpiration = accessExpiration;
    }
}
