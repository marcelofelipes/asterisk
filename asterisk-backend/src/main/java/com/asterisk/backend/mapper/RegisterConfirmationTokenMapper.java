package com.asterisk.backend.mapper;

import com.asterisk.backend.domain.RegisterConfirmationToken;
import com.asterisk.backend.store.user.confirmation.RegisterConfirmationTokenEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegisterConfirmationTokenMapper {

    private final UserMapper userMapper;

    @Autowired
    public RegisterConfirmationTokenMapper(final UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public RegisterConfirmationToken toRegisterConfirmationToken(final RegisterConfirmationTokenEntity entity) {
        final RegisterConfirmationToken registerConfirmationToken = new RegisterConfirmationToken();
        registerConfirmationToken.setId(entity.getId());
        registerConfirmationToken.setExpiresAt(entity.getExpiresAt());
        registerConfirmationToken.setConfirmationCode(entity.getCode());
        registerConfirmationToken.setUser(this.userMapper.toUser(entity.getUser()));
        return registerConfirmationToken;
    }

    public RegisterConfirmationTokenEntity toRegisterConfirmationTokenEntity(final RegisterConfirmationToken registerConfirmationToken) {
        final RegisterConfirmationTokenEntity registerConfirmationTokenEntity = new RegisterConfirmationTokenEntity();
        registerConfirmationTokenEntity.setId(registerConfirmationToken.getId());
        registerConfirmationTokenEntity.setExpiresAt(registerConfirmationToken.getExpiresAt());
        registerConfirmationTokenEntity.setCode(registerConfirmationToken.getConfirmationCode());
        registerConfirmationTokenEntity.setUser(this.userMapper.toUserEntity(registerConfirmationToken.getUser()));
        return registerConfirmationTokenEntity;
    }
}
