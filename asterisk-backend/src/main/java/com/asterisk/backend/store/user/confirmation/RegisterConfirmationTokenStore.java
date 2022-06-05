package com.asterisk.backend.store.user.confirmation;

import com.asterisk.backend.domain.RegisterConfirmationToken;
import com.asterisk.backend.mapper.RegisterConfirmationTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Component
public class RegisterConfirmationTokenStore implements RegisterConfirmationTokenManager {

    private final RegisterConfirmationTokenMapper registerConfirmationTokenMapper;
    private final RegisterConfirmationTokenRepository registerConfirmationTokenRepository;

    @Autowired
    public RegisterConfirmationTokenStore(final RegisterConfirmationTokenMapper registerConfirmationTokenMapper, final RegisterConfirmationTokenRepository registerConfirmationTokenRepository) {
        this.registerConfirmationTokenMapper = registerConfirmationTokenMapper;
        this.registerConfirmationTokenRepository = registerConfirmationTokenRepository;
    }

    @Override
    public RegisterConfirmationToken save(RegisterConfirmationToken confirmationToken) {
        RegisterConfirmationTokenEntity registerConfirmationTokenEntity =
                this.registerConfirmationTokenMapper.toRegisterConfirmationTokenEntity(confirmationToken);
        registerConfirmationTokenEntity =
                this.registerConfirmationTokenRepository.save(registerConfirmationTokenEntity);

        return this.registerConfirmationTokenMapper.toRegisterConfirmationToken(registerConfirmationTokenEntity);
    }

    @Override
    public RegisterConfirmationToken findTokenById(final UUID id) {
        final Optional<RegisterConfirmationTokenEntity> registerConfirmationTokenEntityOptional =
                this.registerConfirmationTokenRepository.findById(id);

        if (registerConfirmationTokenEntityOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }

        final RegisterConfirmationTokenEntity registerConfirmationTokenEntity =
                registerConfirmationTokenEntityOptional.get();
        return this.registerConfirmationTokenMapper.toRegisterConfirmationToken(registerConfirmationTokenEntity);
    }

    @Override
    public void delete(final RegisterConfirmationToken registerConfirmationToken) {
        final RegisterConfirmationTokenEntity registerConfirmationTokenEntity =
                this.registerConfirmationTokenMapper.toRegisterConfirmationTokenEntity(registerConfirmationToken);

        this.registerConfirmationTokenRepository.delete(registerConfirmationTokenEntity);
    }
}
