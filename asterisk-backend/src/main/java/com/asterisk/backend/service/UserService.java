package com.asterisk.backend.service;

import com.asterisk.backend.adapter.authentication.RegisterRequestDto;
import com.asterisk.backend.domain.User;
import com.asterisk.backend.store.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserManager userManager;

    @Autowired
    public UserService(final PasswordEncoder passwordEncoder, final UserManager userManager) {
        this.passwordEncoder = passwordEncoder;
        this.userManager = userManager;
    }

    /**
     *
     * @param registerRequestDto
     * @return
     */
    public User createUser(final RegisterRequestDto registerRequestDto) {
        final User user = new User(registerRequestDto.firstName(), registerRequestDto.lastName(),
                registerRequestDto.username(), registerRequestDto.email(),
                this.passwordEncoder.encode(registerRequestDto.password()));
        return this.userManager.save(user);
    }

    public void saveUser(final User user) {
        this.userManager.save(user);
    }

    public User readUser(final UUID userId) {
        return this.userManager.findUserById(userId);
    }
}
