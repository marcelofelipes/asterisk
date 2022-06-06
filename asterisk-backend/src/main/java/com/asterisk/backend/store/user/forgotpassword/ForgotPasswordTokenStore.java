package com.asterisk.backend.store.user.forgotpassword;

import com.asterisk.backend.domain.ForgotPasswordToken;
import com.asterisk.backend.mapper.ForgotPasswordTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Component
public class ForgotPasswordTokenStore implements ForgotPasswordTokenManager {

    private final ForgotPasswordTokenRepository forgotPasswordTokenRepository;
    private final ForgotPasswordTokenMapper forgotPasswordTokenMapper;

    @Autowired
    public ForgotPasswordTokenStore(final ForgotPasswordTokenRepository forgotPasswordTokenRepository,
                                    final ForgotPasswordTokenMapper forgotPasswordTokenMapper) {
        this.forgotPasswordTokenRepository = forgotPasswordTokenRepository;
        this.forgotPasswordTokenMapper = forgotPasswordTokenMapper;
    }

    @Override
    public ForgotPasswordToken save(final ForgotPasswordToken forgotPasswordToken) {
        ForgotPasswordTokenEntity forgotPasswordTokenEntity =
                this.forgotPasswordTokenMapper.toForgotPasswordTokenEntity(forgotPasswordToken);
        forgotPasswordTokenEntity = this.forgotPasswordTokenRepository.save(forgotPasswordTokenEntity);

        return this.forgotPasswordTokenMapper.toForgotPasswordToken(forgotPasswordTokenEntity);
    }

    @Override
    public ForgotPasswordToken findTokenById(final UUID tokenId) {
        final Optional<ForgotPasswordTokenEntity> forgotPasswordTokenEntity =
                this.forgotPasswordTokenRepository.findById(tokenId);
        if (forgotPasswordTokenEntity.isEmpty()) {
            throw new EntityNotFoundException();
        }
        final ForgotPasswordTokenEntity forgotPasswordToken = forgotPasswordTokenEntity.get();
        return this.forgotPasswordTokenMapper.toForgotPasswordToken(forgotPasswordToken);
    }

    @Override
    public void delete(final ForgotPasswordToken forgotPasswordToken) {
        final ForgotPasswordTokenEntity forgotPasswordTokenEntity =
                this.forgotPasswordTokenMapper.toForgotPasswordTokenEntity(forgotPasswordToken);

        this.forgotPasswordTokenRepository.delete(forgotPasswordTokenEntity);
    }
}
