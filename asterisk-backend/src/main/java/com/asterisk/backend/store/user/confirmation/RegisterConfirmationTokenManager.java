package com.asterisk.backend.store.user.confirmation;

import com.asterisk.backend.domain.RegisterConfirmationToken;

import java.util.UUID;

public interface RegisterConfirmationTokenManager {
    RegisterConfirmationToken save(RegisterConfirmationToken confirmationToken);

    RegisterConfirmationToken findTokenById(UUID confirmationId);

    void delete(RegisterConfirmationToken registerConfirmationToken);
}
