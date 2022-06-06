package com.asterisk.backend.store.user.forgotpassword;

import com.asterisk.backend.domain.ForgotPasswordToken;

import java.util.UUID;

public interface ForgotPasswordTokenManager {
    ForgotPasswordToken save(ForgotPasswordToken forgotPasswordToken);

    ForgotPasswordToken findTokenById(UUID tokenId);

    void delete(ForgotPasswordToken forgotPasswordToken);
}
