package com.asterisk.backend.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public class ForgotPasswordToken extends Timestamp {

    private UUID id;
    private OffsetDateTime expiresAt;
    private User user;

    public ForgotPasswordToken(final OffsetDateTime expiresAt, final User user) {
        this.expiresAt = expiresAt;
        this.user = user;
    }

    public ForgotPasswordToken() {
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public OffsetDateTime getExpiresAt() {
        return this.expiresAt;
    }

    public void setExpiresAt(final OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public boolean isExpired() {
        return OffsetDateTime.now().isAfter(this.expiresAt);
    }
}
