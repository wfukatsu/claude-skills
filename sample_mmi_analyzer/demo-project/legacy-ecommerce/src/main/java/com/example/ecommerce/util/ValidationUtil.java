package com.example.ecommerce.util;

import java.util.regex.Pattern;

/**
 * Validation utility
 * Problem: Should use JSR-303 Bean Validation instead
 */
public class ValidationUtil {

    // Problem: Incomplete email regex
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(.+)@(.+)$");

    // Problem: Incomplete phone regex
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9-]+$");

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Problem: Card number validation is too simple (no Luhn algorithm)
     */
    public static boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            return false;
        }
        // Problem: Just checks length, no actual validation
        String cleaned = cardNumber.replaceAll("[^0-9]", "");
        return cleaned.length() >= 13 && cleaned.length() <= 19;
    }
}
