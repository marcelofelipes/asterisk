package com.asterisk.backend.service;

import com.asterisk.backend._factory.UserTestFactory;
import com.asterisk.backend._integration.IntegrationTest;
import com.asterisk.backend.adapter.rest.authentication.model.PasswordChangeRequestDto;
import com.asterisk.backend.adapter.rest.user.model.UserChangeRequestDto;
import com.asterisk.backend.domain.User;
import com.asterisk.backend.store.user.UserEntity;
import com.asterisk.backend.store.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class UserServiceIT extends IntegrationTest {

    private static final String PASSWORD = "mypassword";

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    @Override
    public void teardown() {
        this.userRepository.deleteAll();
    }

    @Test
    public void testReadUser() {
        // GIVEN
        final UserTestFactory userTestFactory = new UserTestFactory();
        UserEntity user = userTestFactory.newUserEntity();
        user = this.userRepository.save(user);

        // WHEN
        final Optional<User> domainUser = this.userService.readUser(user.getId());

        // THEN
        assertThat(domainUser).isPresent();
        assertThat(domainUser.get())
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(user);
    }

    @Test
    public void testReadUserNotFound() {
        // GIVEN
        final UserTestFactory userTestFactory = new UserTestFactory();
        UserEntity user = userTestFactory.newUserEntity();
        user = this.userRepository.save(user);

        // WHEN
        final Optional<User> domainUser = this.userService.readUser(UUID.randomUUID());

        // THEN
        assertThat(domainUser).isEmpty();
    }

    private static Stream<Arguments> changePasswordRequests() {
        return Stream.of(
                Arguments.of(new PasswordChangeRequestDto("newpassword", "newpassword"), true),
                Arguments.of(new PasswordChangeRequestDto("newpassword", "newpassworddd"), false),
                Arguments.of(new PasswordChangeRequestDto(PASSWORD, PASSWORD), false)

        );
    }

    @ParameterizedTest
    @MethodSource("changePasswordRequests")
    public void testChangePassword(final PasswordChangeRequestDto passwordChangeRequestDto, final boolean exResult) {
        // GIVEN
        final UserTestFactory userTestFactory = new UserTestFactory()
                .setPassword(this.passwordEncoder.encode(PASSWORD));
        UserEntity user = userTestFactory.newUserEntity();
        user = this.userRepository.save(user);

        // WHEN
        final boolean result = this.userService.changePassword(user.getId(), passwordChangeRequestDto);

        assertThat(result).isEqualTo(exResult);
    }

    @Test
    public void testUpdateUser() {
        // GIVEN
        final UserTestFactory userTestFactory = new UserTestFactory()
                .setPassword(this.passwordEncoder.encode(PASSWORD));
        UserEntity user = userTestFactory.newUserEntity();
        user = this.userRepository.save(user);


        final UserChangeRequestDto userChangeRequestDto = new UserChangeRequestDto("nullios", "nullios", null, null);

        // WHEN
        this.userService.updateUser(user.getId(), userChangeRequestDto);

        // THEN
        final UserEntity expected = userTestFactory
                .setFirstName("nullios")
                .setLastName("nullios")
                .newUserEntity();
        user = this.userRepository.getById(user.getId());

        assertThat(user)
                .usingRecursiveComparison()
                .ignoringFields("id","createdAt", "updatedAt")
                .isEqualTo(expected);

    }
}
