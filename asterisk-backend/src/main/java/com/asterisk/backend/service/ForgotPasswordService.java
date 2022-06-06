package com.asterisk.backend.service;

import com.asterisk.backend.application.config.TokenProperties;
import com.asterisk.backend.domain.ForgotPasswordToken;
import com.asterisk.backend.domain.User;
import com.asterisk.backend.store.user.forgotpassword.ForgotPasswordTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class ForgotPasswordService {

    private final TokenProperties tokenProperties;
    private final ForgotPasswordTokenManager forgotPasswordTokenManager;

    @Autowired
    public ForgotPasswordService(final TokenProperties tokenProperties,
                                 final ForgotPasswordTokenManager forgotPasswordTokenManager) {
        this.tokenProperties = tokenProperties;
        this.forgotPasswordTokenManager = forgotPasswordTokenManager;
    }

    public ForgotPasswordToken createToken(final User user) {
        final OffsetDateTime expiresAt =
                OffsetDateTime.now().plusSeconds(this.tokenProperties.getForgotPasswordExpiration());
        final ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken(expiresAt, user);
        return this.forgotPasswordTokenManager.save(forgotPasswordToken);
    }

    public ForgotPasswordToken findToken(UUID tokenId) {
        return this.forgotPasswordTokenManager.findTokenById(tokenId);
    }

    public void deleteToken(ForgotPasswordToken forgotPasswordToken) {
        this.forgotPasswordTokenManager.delete(forgotPasswordToken);
    }
}
