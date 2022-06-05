package com.asterisk.backend.service;

import com.asterisk.backend.adapter.authentication.model.LoginRequestDto;
import com.asterisk.backend.adapter.authentication.model.RegisterConfirmRequestDto;
import com.asterisk.backend.adapter.authentication.model.RegisterRequestDto;
import com.asterisk.backend.domain.RegisterConfirmationToken;
import com.asterisk.backend.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final UserService userService;
    private final RegisterConfirmationService registerConfirmationService;

    @Autowired
    public AuthenticationService(final AuthenticationManager authenticationManager, final EmailService emailService, final UserService userService, final RegisterConfirmationService registerConfirmationService) {
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.userService = userService;
        this.registerConfirmationService = registerConfirmationService;
    }

    /**
     *
     * @param registerRequestDto
     * @return
     */
    public UUID registerNewUser(final RegisterRequestDto registerRequestDto) {
        // Creating and saving
        final User user = this.userService.createUser(registerRequestDto);

        // Confirmation token expires 10 minutes from now
        final RegisterConfirmationToken registerConfirmationToken = this.registerConfirmationService.createToken(user);

        // Send the email
        this.emailService.sendRegisterConfirmationEmail(user, registerConfirmationToken.getConfirmationCode());
        LOGGER.info("Register confirmation email sent to {}", registerRequestDto.email());

        return registerConfirmationToken.getId();
    }

    /**
     *
     * @param loginRequestDto
     * @return
     */
    public Authentication authenticate(final LoginRequestDto loginRequestDto) {
        final String email = loginRequestDto.email();
        final String password = loginRequestDto.password();
        // Attempt login
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Re-Set to current authentication
        authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("User {} successfully authenticated", email);

        return authentication;
    }

    /**
     *
     * @param confirmationId
     * @param registerConfirmRequestDto
     * @return
     */
    public boolean confirmRegistrationOfUser(final UUID confirmationId, final RegisterConfirmRequestDto registerConfirmRequestDto) {
        // Check for token existence
        final RegisterConfirmationToken registerConfirmationToken =
                this.registerConfirmationService.findToken(confirmationId);

        // Check if the token is expired
        if (registerConfirmationToken.isExpired()) {
            this.registerConfirmationService.deleteToken(registerConfirmationToken);
            return false;
        }

        // Check for token matches
        if (!registerConfirmationToken.codeMatches(registerConfirmationToken.getConfirmationCode())) {
            return false;
        }

        // Enable the user
        final User user = registerConfirmationToken.getUser();
        user.setEnabled(true);
        this.userService.saveUser(user);
        this.registerConfirmationService.deleteToken(registerConfirmationToken);
        return true;
    }
}
