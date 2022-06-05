package com.asterisk.backend.domain;

import java.time.OffsetDateTime;

public abstract class Timestamp {

    protected OffsetDateTime createdAt;
    protected OffsetDateTime updatedAt;

    protected Timestamp(final OffsetDateTime createdAt, final OffsetDateTime updatedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    protected Timestamp() {

    }

    public OffsetDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(final OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(final OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
