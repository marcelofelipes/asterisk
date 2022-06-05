package com.asterisk.backend.infrastructure;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfirmationCodeUtilTest {
    // XXX-XXX-XXX
    private static final String REGEX = "^[a-zA-Z0-9]{3}-[a-zA-Z0-9]{3}-[a-zA-Z0-9]{3}$";

    @Test
    public void testConfirmationCodeHasCorrectPattern() {
        // Code should be XXX-XXX-XXX
        final String code = ConfirmationCodeUtil.generateRegisterConfirmationCode();
        assertThat(code).matches(Pattern.compile(REGEX));
    }
}
