package com.asterisk.backend.service;

import com.asterisk.backend._factory.UserTestFactory;
import com.asterisk.backend._integration.IntegrationTest;
import com.asterisk.backend.domain.User;
import com.asterisk.backend.store.user.UserEntity;
import com.asterisk.backend.store.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class UserServiceIT extends IntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testReadUser() {
        // GIVEN
        final UserTestFactory userTestFactory = new UserTestFactory();
        UserEntity user = userTestFactory.newUserEntity();
        user = this.userRepository.save(user);

        // WHEN
        final User domainUser = this.userService.readUser(user.getId());

        // THEN
        assertThat(domainUser).isNotNull();
        assertThat(domainUser)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(user);
    }
}
