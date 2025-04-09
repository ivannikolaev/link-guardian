package com.github.ivannikolaev.link_guardian.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class CodeGenerator {
    private static final String CODE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final Random random = new SecureRandom();

    public String generateCode(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            code.append(CODE_CHARS.charAt(random.nextInt(CODE_CHARS.length())));
        }
        return code.toString();
    }
}
