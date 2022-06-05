package com.asterisk.backend.infrastructure;

import java.security.SecureRandom;

public class ConfirmationCodeUtil {

    private static final String SEPARATOR = "-";
    private static final int LENGTH = 11;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Generates a new confirmation code of pattern XXX-XXX-XXX
     *
     * @return code as string
     */
    public static String generateRegisterConfirmationCode() {
        final StringBuilder buffer = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            if (i == 3 || i == 7) {
                buffer.append(SEPARATOR);
            } else {
                buffer.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
            }
        }
        return new String(buffer);
    }
}
