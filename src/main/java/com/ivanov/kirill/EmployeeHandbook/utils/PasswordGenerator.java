package com.ivanov.kirill.EmployeeHandbook.utils;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;

public final class PasswordGenerator {
    private static final org.passay.PasswordGenerator gen = new org.passay.PasswordGenerator();
    private static final CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
    private static final CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);

    private static final CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
    private static final CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);

    private static final CharacterData digitChars = EnglishCharacterData.Digit;
    private static final CharacterRule digitRule = new CharacterRule(digitChars);


    public static String generate() {
        lowerCaseRule.setNumberOfCharacters(2);
        upperCaseRule.setNumberOfCharacters(2);
        digitRule.setNumberOfCharacters(2);
        return gen.generatePassword(16, lowerCaseRule, upperCaseRule, digitRule);
    }
}
