package com.asterisk.backend.application.security.domain;

import com.asterisk.backend.application.common.UserDetailsImpl;
import com.asterisk.backend.domain.User;
import com.asterisk.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserSecurity {

    private final UserService userService;

    @Autowired
    public UserSecurity(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Checks whether the id of the current authentication principal matches the requested user
     * @param authentication authentication
     * @param userId requested user id
     * @return true if both match
     */
    public boolean isAccountOwner(final Authentication authentication, final UUID userId) {
        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        final Optional<User> userOptional = this.userService.readUser(userId);

        if (userOptional.isEmpty()) return false;

        return userOptional.get().getId().equals(userDetails.getId());
    }
}
