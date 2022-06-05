package com.asterisk.backend.store.user;

import com.asterisk.backend.domain.User;
import com.asterisk.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserStore implements UserManager{

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserStore(final UserRepository userRepository, final UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User findUserByEmail(final String email) {
        final Optional<UserEntity> userEntity = this.userRepository.findByEmail(email);

        if (userEntity.isEmpty()) return null;

        return this.userMapper.toUser(userEntity.get());
    }

    @Override
    public User save(final User user) {
        UserEntity userEntity = this.userMapper.toUserEntity(user);
        userEntity = this.userRepository.save(userEntity);

        return this.userMapper.toUser(userEntity);
    }

    @Override
    public User findUserById(final UUID userId) {
        final Optional<UserEntity> userEntity = this.userRepository.findById(userId);

        if (userEntity.isEmpty()) return null;

        return this.userMapper.toUser(userEntity.get());
    }
}
