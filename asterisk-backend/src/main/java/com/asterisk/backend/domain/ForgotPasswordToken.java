package com.asterisk.backend.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public class ForgotPasswordToken extends Timestamp {

    private UUID id;
    private OffsetDateTime expiresAt;
    private User user;
}
