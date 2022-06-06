package com.asterisk.backend.service;

import com.asterisk.backend.application.config.TokenProperties;
import com.asterisk.backend.domain.RegisterConfirmationToken;
import com.asterisk.backend.domain.User;
import com.asterisk.backend.infrastructure.ConfirmationCodeUtil;
import com.asterisk.backend.store.user.confirmation.RegisterConfirmationTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class RegisterConfirmationService {

    private final RegisterConfirmationTokenManager registerConfirmationTokenManager;
    private final TokenProperties tokenProperties;

    @Autowired
    public RegisterConfirmationService(final RegisterConfirmationTokenManager registerConfirmationTokenManager, final TokenProperties tokenProperties) {
        this.registerConfirmationTokenManager = registerConfirmationTokenManager;
        this.tokenProperties = tokenProperties;
    }

    public RegisterConfirmationToken createToken(final User user) {
        // Confirmation token expires 10 minutes from now
        final OffsetDateTime expiresAt =
                OffsetDateTime.now().plusSeconds(this.tokenProperties.getRegisterConfirmationExpiration());
        final String confirmationCode = ConfirmationCodeUtil.generateRegisterConfirmationCode();
        final RegisterConfirmationToken confirmationToken = new RegisterConfirmationToken(expiresAt,
                confirmationCode, user);
        return this.registerConfirmationTokenManager.save(confirmationToken);
    }

    public RegisterConfirmationToken findToken(final UUID confirmationId) {
        return this.registerConfirmationTokenManager.findTokenById(confirmationId);
    }

    public void deleteToken(final RegisterConfirmationToken registerConfirmationToken) {
        this.registerConfirmationTokenManager.delete(registerConfirmationToken);
    }

    public RegisterConfirmationToken saveToken(final RegisterConfirmationToken token) {
        return this.registerConfirmationTokenManager.save(token);
    }
}
