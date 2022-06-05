package com.asterisk.backend.application.jwt;

import com.asterisk.backend._integration.IntegrationTest;
import com.asterisk.backend._integration.authentication.WithAsteriskUser;
import com.asterisk.backend.application.security.jwt.JwtHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtHelperIT extends IntegrationTest {
    public static final String EMAIL = "john@doe.com";

    @Autowired
    private JwtHelper jwtHelper;

    @Test
    public void testGenerateFingerprint() {
        // WHEN
        final String fingerprint = this.jwtHelper.generateFingerprint();

        // THEN
        assertThat(fingerprint).isNotNull();
        assertThat(fingerprint.length()).isEqualTo(100);
    }

    @Test
    public void testGenerateFingerprintHash() {
        // GIVEN
        final String fingerprint = this.jwtHelper.generateFingerprint();

        // WHEN
        final String hashedFingerprint = this.jwtHelper.generateFingerprintHash(fingerprint);

        // THEN
        assertThat(hashedFingerprint).isNotNull();
    }

    @Test
    public void testGenerateFingerprintCookie() {
        // GIVEN
        final String fingerprint = this.jwtHelper.generateFingerprint();

        // WHEN
        final ResponseCookie responseCookie = this.jwtHelper.generateFingerprintCookie(fingerprint);

        // THEN
        assertThat(responseCookie.getValue()).isEqualTo(fingerprint);
    }

    @Test
    @WithAsteriskUser
    public void testGenerateAccessJwt() {
        // GIVEN
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String fingerprint = this.jwtHelper.generateFingerprint();
        final String hashedFingerprint = this.jwtHelper.generateFingerprintHash(fingerprint);

        // WHEN
        final String accessJwt = this.jwtHelper.generateAccessJwt(authentication, hashedFingerprint);

        // THEN
        assertThat(accessJwt).isNotNull();
    }

    @Test
    @WithAsteriskUser(email = EMAIL)
    public void testGetEmailFromAccessJwt() {
        // GIVEN
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String fingerprint = this.jwtHelper.generateFingerprint();
        final String hashedFingerprint = this.jwtHelper.generateFingerprintHash(fingerprint);
        final String accessJwt = this.jwtHelper.generateAccessJwt(authentication, hashedFingerprint);

        // WHEN
        final String email = this.jwtHelper.getEmailFromAccessJwt(accessJwt);

        // THEN
        assertThat(email).isNotNull();
        assertThat(email).isEqualTo(EMAIL);
    }


    @Test
    @WithAsteriskUser
    public void testValidateAccessJwtSuccess() {
        // GIVEN
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String fingerprint = this.jwtHelper.generateFingerprint();
        final String hashedFingerprint = this.jwtHelper.generateFingerprintHash(fingerprint);
        final String accessJwt = this.jwtHelper.generateAccessJwt(authentication, hashedFingerprint);

        // WHEN
        final boolean result = this.jwtHelper.validateAccessJwt(accessJwt, fingerprint);

        // THEN
        assertThat(result).isTrue();
    }

    @Test
    public void testValidateAccessJwtInvalidToken() {
        // GIVEN
        final String fingerprint = this.jwtHelper.generateFingerprint();

        // WHEN
        final boolean result = this.jwtHelper.validateAccessJwt("gibberish", fingerprint);

        // THEN
        assertThat(result).isFalse();
    }

    @Test
    @WithAsteriskUser
    public void testValidateAccessJwtInvalidFingerprint() {
        // GIVEN
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String fingerprint = this.jwtHelper.generateFingerprint();
        final String hashedFingerprint = this.jwtHelper.generateFingerprintHash(fingerprint);
        final String accessJwt = this.jwtHelper.generateAccessJwt(authentication, hashedFingerprint);

        // WHEN
        final boolean result = this.jwtHelper.validateAccessJwt(accessJwt, "gibberish");

        // THEN
        assertThat(result).isFalse();
    }

    @Test
    @WithAsteriskUser
    public void testValidateAccessJwtExpired() throws InterruptedException {
        // GIVEN
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String fingerprint = this.jwtHelper.generateFingerprint();
        final String hashedFingerprint = this.jwtHelper.generateFingerprintHash(fingerprint);
        final String accessJwt = this.jwtHelper.generateAccessJwt(authentication, hashedFingerprint);

        Thread.sleep(2000);

        // WHEN
        final boolean result = this.jwtHelper.validateAccessJwt(accessJwt, fingerprint);

        // THEN
        assertThat(result).isFalse();
    }
}
