package com.asterisk.backend.mapper;

import com.asterisk.backend.domain.ForgotPasswordToken;
import com.asterisk.backend.store.user.forgotpassword.ForgotPasswordTokenEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ForgotPasswordTokenMapper {

    private final UserMapper userMapper;

    @Autowired
    public ForgotPasswordTokenMapper(final UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public ForgotPasswordToken toForgotPasswordToken(final ForgotPasswordTokenEntity entity) {
        final ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
        forgotPasswordToken.setId(entity.getId());
        forgotPasswordToken.setExpiresAt(entity.getExpiresAt());
        forgotPasswordToken.setUser(this.userMapper.toUser(entity.getUser()));
        forgotPasswordToken.setCreatedAt(entity.getCreatedAt());
        forgotPasswordToken.setUpdatedAt(entity.getUpdatedAt());
        return forgotPasswordToken;
    }


    public ForgotPasswordTokenEntity toForgotPasswordTokenEntity(final ForgotPasswordToken token) {
        final ForgotPasswordTokenEntity forgotPasswordToken = new ForgotPasswordTokenEntity();
        forgotPasswordToken.setId(token.getId());
        forgotPasswordToken.setExpiresAt(token.getExpiresAt());
        forgotPasswordToken.setUser(this.userMapper.toUserEntity(token.getUser()));
        forgotPasswordToken.setCreatedAt(token.getCreatedAt());
        forgotPasswordToken.setUpdatedAt(token.getUpdatedAt());
        return forgotPasswordToken;
    }
}
