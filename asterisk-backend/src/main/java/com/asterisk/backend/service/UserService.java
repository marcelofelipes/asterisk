package com.asterisk.backend.service;

import com.asterisk.backend.adapter.authentication.model.PasswordChangeRequestDto;
import com.asterisk.backend.adapter.authentication.model.RegisterRequestDto;
import com.asterisk.backend.adapter.user.model.UserChangeRequestDto;
import com.asterisk.backend.domain.User;
import com.asterisk.backend.infrastructure.exception.UserNotFoundException;
import com.asterisk.backend.store.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
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
     * Creates a new (not enabled) user
     * @param registerRequestDto user data
     * @return the created user
     */
    public User createUser(final RegisterRequestDto registerRequestDto) {
        final User user = new User(registerRequestDto.firstName(), registerRequestDto.lastName(),
                registerRequestDto.username(), registerRequestDto.email(),
                this.passwordEncoder.encode(registerRequestDto.password()), registerRequestDto.phone());
        return this.userManager.save(user);
    }

    /**
     * Changes the password of a given user
     * @param userId user
     * @param passwordChangeRequestDto new password
     * @return true if successful
     */
    public boolean changePassword(final UUID userId, final PasswordChangeRequestDto passwordChangeRequestDto) {
        final Optional<User> userOptional = this.readUser(userId);

        if (userOptional.isEmpty()) return false;

        final User user = userOptional.get();
        // Check if both passwords match
        if (!passwordChangeRequestDto.password().equals(passwordChangeRequestDto.passwordConfirmation())) {
            return false;
        }

        // 'New password is old password'
        if (this.passwordEncoder.matches(passwordChangeRequestDto.password(), user.getPassword())) {
            return false;
        }
        // Set new password
        user.setPassword(this.passwordEncoder.encode(passwordChangeRequestDto.password()));
        this.saveUser(user);
        return true;
    }

    /**
     *
     * @param userId
     * @param userChangeRequestDto
     */
    public boolean updateUser(final UUID userId, final UserChangeRequestDto userChangeRequestDto) {
        final Optional<User> userOptional = this.readUser(userId);

        if (userOptional.isEmpty()) return false;

        final User user = userOptional.get();

        if (userChangeRequestDto.firstName() != null) {
            user.setFirstName(userChangeRequestDto.firstName());
        }

        if (userChangeRequestDto.lastName() != null) {
            user.setLastName(userChangeRequestDto.lastName());
        }

        if (userChangeRequestDto.email() != null) {
            user.setEmail(userChangeRequestDto.email());
        }

        if (userChangeRequestDto.username() != null) {
            user.setUsername(userChangeRequestDto.username());
        }

        this.saveUser(user);
        return true;
    }

    /**
     *
     * @param userId
     * @return
     */
    public Optional<User> readUser(final UUID userId) {
        try {
            final User user = this.userManager.findUserById(userId);
            return Optional.of(user);
        } catch (final UserNotFoundException e) {
            return Optional.empty();
        }
    }

    /**
     *
     * @param email
     * @return
     */
    public Optional<User> findByEmail(final String email) {
        try {
            final User user = this.userManager.findUserByEmail(email);
            return Optional.of(user);
        } catch (final UserNotFoundException e) {
            return Optional.empty();
        }
    }

    /**
     *
     * @param user
     */
    public void saveUser(final User user) {
        this.userManager.save(user);
    }
}
