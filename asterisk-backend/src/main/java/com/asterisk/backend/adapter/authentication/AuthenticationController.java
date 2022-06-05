package com.asterisk.backend.adapter.authentication;

import com.asterisk.backend.adapter.ResponseDto;
import com.asterisk.backend.application.security.jwt.JwtHelper;
import com.asterisk.backend.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtHelper jwtHelper;

    @Autowired
    public AuthenticationController(final AuthenticationService authenticationService, final JwtHelper jwtHelper) {
        this.authenticationService = authenticationService;
        this.jwtHelper = jwtHelper;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@Valid @RequestBody final RegisterRequestDto registerRequestDto) {
        final UUID confirmationTokenId = this.authenticationService.registerNewUser(registerRequestDto);
        final ResponseDto responseDto = new ResponseDto("A register confirmation email was sent.", confirmationTokenId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<ResponseDto> login(@Valid @RequestBody final LoginRequestDto loginRequestDto) {
        try {
            final Authentication authentication = this.authenticationService.authenticate(loginRequestDto);
            // Fingerprints
            final String fingerprint = this.jwtHelper.generateFingerprint();
            final String hash = this.jwtHelper.generateFingerprintHash(fingerprint);

            // Getting sent along with response
            final ResponseCookie cookie = this.jwtHelper.generateFingerprintCookie(fingerprint);
            final String accessJwt = this.jwtHelper.generateAccessJwt(authentication, hash);

            final HttpHeaders headers = new HttpHeaders();
            headers.add("Set-Cookie", cookie.toString());
            final ResponseDto responseDto = new ResponseDto("Login successful", accessJwt);
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(responseDto);
        } catch (final BadCredentialsException | UsernameNotFoundException e) {
            final ResponseDto responseDto = new ResponseDto("Credentials dont match.", null);
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<?> logout() {
        final ResponseCookie responseCookie = ResponseCookie
                .from("fgp", "")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(0L)
                .sameSite("Strict")
                .build();
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", responseCookie.toString());
        return ResponseEntity.status(HttpStatus.OK).headers(headers).build();
    }

    @PostMapping(value = "/register/{confirmationId}/confirm")
    public ResponseEntity<?> confirmRegistration(@PathVariable final UUID confirmationId,
                                                 @Valid @RequestBody final RegisterConfirmRequestDto registerConfirmRequestDto) {
        final boolean result = this.authenticationService.confirmRegistrationOfUser(confirmationId,
                registerConfirmRequestDto);
        if (!result) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

}
