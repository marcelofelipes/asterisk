package com.asterisk.backend.application.security.domain;

import com.asterisk.backend.application.common.UserDetailsImpl;
import com.asterisk.backend.domain.User;
import com.asterisk.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserSecurity {

    private final UserService userService;

    @Autowired
    public UserSecurity(final UserService userService) {
        this.userService = userService;
    }

    /**
     *
     * @param authentication
     * @param userId
     * @return
     */
    public boolean isAccountOwner(final Authentication authentication, final UUID userId) {
        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        final User user = this.userService.readUser(userId);
        return user.getId().equals(userDetails.getId());
    }
}
