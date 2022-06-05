package com.asterisk.backend.application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "asterisk.token")
public class TokenProperties {

    private Long registerConfirmationExpiration;
    private Long forgotPasswordExpiration;

    public TokenProperties() {

    }

    public Long getRegisterConfirmationExpiration() {
        return this.registerConfirmationExpiration;
    }

    public void setRegisterConfirmationExpiration(final Long registerConfirmationExpiration) {
        this.registerConfirmationExpiration = registerConfirmationExpiration;
    }

    public Long getForgotPasswordExpiration() {
        return this.forgotPasswordExpiration;
    }

    public void setForgotPasswordExpiration(final Long forgotPasswordExpiration) {
        this.forgotPasswordExpiration = forgotPasswordExpiration;
    }
}
