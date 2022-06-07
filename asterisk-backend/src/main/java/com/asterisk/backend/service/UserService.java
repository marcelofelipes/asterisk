package com.asterisk.backend.service;

import com.asterisk.backend.adapter.authentication.model.PasswordChangeRequestDto;
import com.asterisk.backend.adapter.authentication.model.RegisterRequestDto;
import com.asterisk.backend.adapter.user.model.UserChangeRequestDto;
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
     * @param registerRequestDto
     * @return
     */
    public User createUser(final RegisterRequestDto registerRequestDto) {
        final User user = new User(registerRequestDto.firstName(), registerRequestDto.lastName(),
                registerRequestDto.username(), registerRequestDto.email(),
                this.passwordEncoder.encode(registerRequestDto.password()));
        return this.userManager.save(user);
    }

    /**
     * @param userId
     * @param passwordChangeRequestDto
     * @return
     */
    public boolean changePassword(final UUID userId, final PasswordChangeRequestDto passwordChangeRequestDto) {
        final User user = this.readUser(userId);
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
    public void updateUser(final UUID userId, final UserChangeRequestDto userChangeRequestDto) {
        final User user = this.readUser(userId);

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
    }

    public void saveUser(final User user) {
        this.userManager.save(user);
    }

    public User readUser(final UUID userId) {
        return this.userManager.findUserById(userId);
    }

    public User findByEmail(final String email) {
        return this.userManager.findUserByEmail(email);
    }
}
