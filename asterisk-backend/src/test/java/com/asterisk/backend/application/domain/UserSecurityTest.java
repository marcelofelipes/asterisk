package com.asterisk.backend.application.domain;

import com.asterisk.backend._factory.UserTestFactory;
import com.asterisk.backend._integration.authentication.WithFakeAsteriskUser;
import com.asterisk.backend.application.security.domain.UserSecurity;
import com.asterisk.backend.domain.User;
import com.asterisk.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserSecurityTest {
    static final String USER_ID = "550e8400-e29b-11d4-a716-446655440000";

    @MockBean
    private UserService userService;

    private UserSecurity userSecurity;

    @BeforeEach
    public void setUp() {
        this.userSecurity = new UserSecurity(this.userService);
    }

    @Test
    @WithFakeAsteriskUser(id = USER_ID)
    public void testIsAccountOwnerValid() {
        // GIVEN
        final UUID id = UUID.fromString(USER_ID);
        final User user = new UserTestFactory().setId(id).newDomainUser();

        when(this.userService.readUser(id)).thenReturn(user);

        // WHEN
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final boolean result = this.userSecurity.isAccountOwner(authentication, id);

        // THEN
        assertThat(result).isTrue();
    }

    @Test
    @WithFakeAsteriskUser(id = USER_ID)
    public void testIsAccountOwnerInvalid() {
        // GIVEN
        final User user = new UserTestFactory().newDomainUser();

        when(this.userService.readUser(user.getId())).thenReturn(user);

        // WHEN
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final boolean result = this.userSecurity.isAccountOwner(authentication, user.getId());

        // THEN
        assertThat(result).isFalse();
    }
}
