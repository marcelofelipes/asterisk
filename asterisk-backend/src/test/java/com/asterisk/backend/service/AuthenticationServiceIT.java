package com.asterisk.backend.service;

import com.asterisk.backend._factory.UserTestFactory;
import com.asterisk.backend._integration.IntegrationTest;
import com.asterisk.backend.adapter.authentication.model.LoginRequestDto;
import com.asterisk.backend.store.user.UserEntity;
import com.asterisk.backend.store.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthenticationServiceIT extends IntegrationTest {

    private static final String PASSWORD = "passwordpassword";

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserEntity enabledUser;

    @BeforeEach
    @Override
    public void setUp() {
        final UserEntity enabledUserEntity = new UserTestFactory()
                .setPassword(this.passwordEncoder.encode(PASSWORD))
                .newUserEntity();

        this.enabledUser = this.userRepository.save(enabledUserEntity);
    }

    @Test
    public void testLoginSuccessful() {
        // GIVEN
        final LoginRequestDto loginRequestDto = new LoginRequestDto(this.enabledUser.getEmail(), PASSWORD);

        // WHEN
        final Authentication authentication = this.authenticationService.authenticate(loginRequestDto);

        // THEN
        assertThat(authentication).isNotNull();
    }
}
