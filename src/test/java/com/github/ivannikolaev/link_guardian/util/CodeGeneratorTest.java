package com.github.ivannikolaev.link_guardian.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CodeGeneratorTest {

    private final CodeGenerator codeGenerator = new CodeGenerator();

    @Test
    void shouldGenerateCodeOfSpecifiedLength() {
        String code = codeGenerator.generateCode(10);
        assertEquals(10, code.length());
    }

    @Test
    void shouldGenerateOnlyValidCharacters() {
        String code = codeGenerator.generateCode(10);
        assertTrue(code.chars().allMatch(Character::isLetterOrDigit));
    }

    @Test
    void shouldThrowExceptionForZeroLength() {
        assertThrows(IllegalArgumentException.class, () -> codeGenerator.generateCode(0));
    }

}