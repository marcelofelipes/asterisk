package com.asterisk.backend.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public class RegisterConfirmationToken extends Timestamp {
    private UUID id;
    private OffsetDateTime expiresAt;
    private String confirmationCode;
    private User user;

    public RegisterConfirmationToken(final OffsetDateTime expiresAt, final String confirmationCode, final User user) {
        super();
        this.expiresAt = expiresAt;
        this.confirmationCode = confirmationCode;
        this.user = user;
    }

    public RegisterConfirmationToken() {
    }

    public boolean codeMatches(final String confirmationCode) {
        return this.confirmationCode.equals(confirmationCode);

    }

    public boolean isExpired() {
        return OffsetDateTime.now().isAfter(this.expiresAt);
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

    public String getConfirmationCode() {
        return this.confirmationCode;
    }

    public void setConfirmationCode(final String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(final User user) {
        this.user = user;
    }
}
