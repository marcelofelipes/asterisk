package com.asterisk.backend.service;

import com.asterisk.backend._factory.UserTestFactory;
import com.asterisk.backend._integration.IntegrationTest;
import com.asterisk.backend.adapter.authentication.model.*;
import com.asterisk.backend.domain.User;
import com.asterisk.backend.infrastructure.ConfirmationCodeUtil;
import com.asterisk.backend.store.user.UserEntity;
import com.asterisk.backend.store.user.UserRepository;
import com.asterisk.backend.store.user.confirmation.RegisterConfirmationTokenEntity;
import com.asterisk.backend.store.user.confirmation.RegisterConfirmationTokenRepository;
import com.asterisk.backend.store.user.forgotpassword.ForgotPasswordTokenRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AuthenticationServiceIT extends IntegrationTest {

    private static final String PASSWORD = "passwordpassword";
    private static final String EMAIL = "john@doe.com";

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegisterConfirmationTokenRepository registerConfirmationTokenRepository;

    @Autowired
    private ForgotPasswordTokenRepository forgotPasswordTokenRepository;

    @MockBean
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<String> confirmationCodeCaptor;

    @Captor
    private ArgumentCaptor<UUID> forgotPasswordTokenIdCaptor;

    private UserEntity enabledUser;

    @BeforeEach
    @Override
    public void setUp() {
        final UserEntity enabledUserEntity = new UserTestFactory()
                .setEmail(EMAIL)
                .setPassword(this.passwordEncoder.encode(PASSWORD))
                .newUserEntity();

        this.enabledUser = this.userRepository.save(enabledUserEntity);
    }

    @AfterEach
    @Override
    public void teardown() {
        this.userRepository.deleteAll();
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

    @Test
    public void testLoginUserNotEnabled() {
        // GIVEN
        UserEntity disabledUser = new UserTestFactory()
                .setEmail("bla@blub.com")
                .setUsername("blablub")
                .setEnabled(false)
                .setPassword(this.passwordEncoder.encode(PASSWORD))
                .newUserEntity();
        disabledUser = this.userRepository.save(disabledUser);
        final LoginRequestDto loginRequestDto = new LoginRequestDto(disabledUser.getEmail(), PASSWORD);

        // WHEN - THEN
        assertThrows(DisabledException.class, () -> this.authenticationService.authenticate(loginRequestDto));
    }

    private static Stream<Arguments> loginCredentials() {
        return Stream.of(
                Arguments.of(EMAIL, "wrongpassword"),
                Arguments.of("wrong@email.com", PASSWORD)
        );
    }

    @ParameterizedTest
    @MethodSource("loginCredentials")
    public void testLoginBadCredentials(final String email, final String password) {
        // GIVEN
        final LoginRequestDto loginRequestDto = new LoginRequestDto(email, password);

        // WHEN - THEN
        assertThrows(BadCredentialsException.class, () -> this.authenticationService.authenticate(loginRequestDto));
    }

    @Test
    public void testRegisterUserSuccess() {
        // GIVEN - WHEN
        final UUID confirmationId = this.assertRegisterSuccess();

        // THEN
        final Optional<RegisterConfirmationTokenEntity> confirmationTokenEntity =
                this.registerConfirmationTokenRepository.findById(confirmationId);
        assertThat(confirmationTokenEntity).isPresent();

        final RegisterConfirmationTokenEntity entity = confirmationTokenEntity.get();

        assertThat(confirmationId).isEqualTo(entity.getId());
        assertThat(this.confirmationCodeCaptor.getValue()).isEqualTo(entity.getCode());
    }

    @Test
    public void testConfirmRegistrationSuccess() {
        // First part - registering an account
        final UUID confirmationId = this.assertRegisterSuccess();

        final RegisterConfirmationTokenEntity entity = this.registerConfirmationTokenRepository.getById(confirmationId);
        UserEntity user = entity.getUser();

        // Second part - confirming the newly registered account
        // GIVEN
        final RegisterConfirmRequestDto registerConfirmRequestDto =
                new RegisterConfirmRequestDto(this.confirmationCodeCaptor.getValue());

        // WHEN
        final boolean result = this.authenticationService.confirmRegistrationOfUser(confirmationId,
                registerConfirmRequestDto);

        // THEN
        assertThat(result).isTrue();

        user = this.userRepository.getById(user.getId());
        assertThat(user.isEnabled()).isTrue();

        final Optional<RegisterConfirmationTokenEntity> tokenEntity =
                this.registerConfirmationTokenRepository.findById(confirmationId);
        assertThat(tokenEntity).isEmpty();
    }

    @Test
    public void testConfirmRegistrationTokenExpired() throws InterruptedException {
        // First part - registering an account
        final UUID confirmationId = this.assertRegisterSuccess();

        // GIVEN
        final RegisterConfirmRequestDto registerConfirmRequestDto =
                new RegisterConfirmRequestDto(this.confirmationCodeCaptor.getValue());

        Thread.sleep(this.tokenProperties.getRegisterConfirmationExpiration() * 1000);

        // WHEN
        final boolean result = this.authenticationService.confirmRegistrationOfUser(confirmationId,
                registerConfirmRequestDto);

        // THEN
        assertThat(result).isFalse();
    }

    @Test
    public void testConfirmRegistrationNoCodeMatch() {
        // First part - registering an account
        final UUID confirmationId = this.assertRegisterSuccess();

        // GIVEN
        final RegisterConfirmRequestDto registerConfirmRequestDto =
                new RegisterConfirmRequestDto(ConfirmationCodeUtil.generateRegisterConfirmationCode());

        // WHEN
        final boolean result = this.authenticationService.confirmRegistrationOfUser(confirmationId,
                registerConfirmRequestDto);

        // THEN
        assertThat(result).isFalse();
    }

    @Test
    public void testConfirmRegistrationResendCode() {
        // First part - registering an account
        final UUID confirmationId = this.assertRegisterSuccess();

        reset(this.emailService);

        RegisterConfirmationTokenEntity entity = this.registerConfirmationTokenRepository.getById(confirmationId);
        final String oldCode = entity.getCode();

        // WHEN
        final boolean result = this.authenticationService.resendConfirmationCode(confirmationId);

        // THEN
        verify(this.emailService, times(1)).sendRegisterConfirmationEmail(any(User.class),
                this.confirmationCodeCaptor.capture());

        assertThat(result).isTrue();
        entity = this.registerConfirmationTokenRepository.getById(confirmationId);

        assertThat(entity.getCode()).isNotEqualTo(oldCode);
    }

    @Test
    public void testConfirmRegistrationResendCodeTokenExpired() throws InterruptedException {
        // First part - registering an account
        final UUID confirmationId = this.assertRegisterSuccess();

        reset(this.emailService);
        Thread.sleep(this.tokenProperties.getRegisterConfirmationExpiration() * 1000);

        // WHEN
        final boolean result = this.authenticationService.resendConfirmationCode(confirmationId);

        // THEN
        assertThat(result).isFalse();
        verify(this.emailService, times(0)).sendRegisterConfirmationEmail(any(User.class), any(String.class));
    }

    @Test
    public void testConfirmRegistrationResendCodeTokenNotFound() throws InterruptedException {
        // First part - registering an account
        this.assertRegisterSuccess();

        reset(this.emailService);

        // WHEN
        final boolean result = this.authenticationService.resendConfirmationCode(UUID.randomUUID());

        // THEN
        assertThat(result).isFalse();
        verify(this.emailService, times(0)).sendRegisterConfirmationEmail(any(User.class), any(String.class));
    }

    @Test
    public void testForgotPasswordSuccess() {
        // Perform a forgot password request
        final UUID forgotPasswordTokenId = this.assertForgotPasswordRequestSuccess();

        assertThat(forgotPasswordTokenId).isEqualTo(this.forgotPasswordTokenIdCaptor.getValue());
        assertThat(this.forgotPasswordTokenRepository.findById(forgotPasswordTokenId)).isPresent();
    }

    @Test
    public void testForgotPasswordUserNotFound() {
        // GIVEN
        final UserTestFactory userTestFactory = new UserTestFactory()
                .setEmail("mister2@mail.com")
                .setUsername("someone2");
        ;
        final UserEntity user = userTestFactory.newUserEntity();
        this.userRepository.save(user);

        final PasswordForgetRequestDto passwordForgetRequestDto = new PasswordForgetRequestDto("some@email.com");

        // WHEN
        final Optional<UUID> forgotPasswordTokenId =
                this.authenticationService.generateForgotPasswordToken(passwordForgetRequestDto);

        // THEN
        verify(this.emailService, times(0))
                .sendForgotPasswordEmail(any(User.class), any(UUID.class));
        assertThat(forgotPasswordTokenId).isEmpty();
    }

    @Test
    public void testResetPasswordSuccess() {
        // Perform a forgot password request
        final UUID forgotPasswordTokenId = this.assertForgotPasswordRequestSuccess();

        // GIVEN
        final PasswordChangeRequestDto passwordChangeRequestDto = new PasswordChangeRequestDto("newpassword",
                "newpassword");

        // WHEN
        final boolean result = this.authenticationService.resetPassword(forgotPasswordTokenId,
                passwordChangeRequestDto);

        assertThat(result).isTrue();
    }

    @Test
    public void testResetPasswordTokenNotFound() {
        // Perform a forgot password request
        this.assertForgotPasswordRequestSuccess();

        // GIVEN
        final PasswordChangeRequestDto passwordChangeRequestDto = new PasswordChangeRequestDto("newpassword",
                "newpassword");

        // WHEN
        final boolean result = this.authenticationService.resetPassword(UUID.randomUUID(),
                passwordChangeRequestDto);

        assertThat(result).isFalse();
    }

    @Test
    public void testResetPasswordTokenExpired() throws InterruptedException {
        // Perform a forgot password request
        this.assertForgotPasswordRequestSuccess();

        // GIVEN
        final PasswordChangeRequestDto passwordChangeRequestDto = new PasswordChangeRequestDto("newpassword",
                "newpassword");

        Thread.sleep(this.tokenProperties.getForgotPasswordExpiration() * 1000);

        // WHEN
        final boolean result = this.authenticationService.resetPassword(UUID.randomUUID(),
                passwordChangeRequestDto);

        assertThat(result).isFalse();
    }

    /**
     * Performs a successful registration
     *
     * @return confirmation id of the token generated
     */
    private UUID assertRegisterSuccess() {
        // GIVEN
        final UserTestFactory userTestFactory = new UserTestFactory()
                .setEmail("foo@foos.com")
                .setUsername("foooooo");
        final RegisterRequestDto registerRequestDto = userTestFactory.newRegisterRequestDto();

        // WHEN
        final UUID confirmationId = this.authenticationService.registerNewUser(registerRequestDto);

        // THEN
        verify(this.emailService, times(1)).sendRegisterConfirmationEmail(any(User.class),
                this.confirmationCodeCaptor.capture());

        return confirmationId;
    }

    private UUID assertForgotPasswordRequestSuccess() {
        final UserTestFactory userTestFactory = new UserTestFactory()
                .setEmail("mister@mail.com")
                .setUsername("someone");
        UserEntity user = userTestFactory.newUserEntity();
        user = this.userRepository.save(user);

        final PasswordForgetRequestDto passwordForgetRequestDto = new PasswordForgetRequestDto(user.getEmail());

        // WHEN
        final Optional<UUID> forgotPasswordTokenId =
                this.authenticationService.generateForgotPasswordToken(passwordForgetRequestDto);

        // THEN
        verify(this.emailService, times(1))
                .sendForgotPasswordEmail(any(User.class), this.forgotPasswordTokenIdCaptor.capture());
        assertThat(forgotPasswordTokenId).isPresent();

        return forgotPasswordTokenId.get();
    }
}
