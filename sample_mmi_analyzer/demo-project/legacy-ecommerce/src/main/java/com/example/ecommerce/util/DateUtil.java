package com.example.ecommerce.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Date utility class
 * Problem: Unnecessary utility - Java 8+ has built-in date handling
 */
public class DateUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Problem: Duplicates LocalDateTime.now()
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    /**
     * Problem: Not thread-safe if formatter is modified
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }

    public static LocalDateTime parseDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, FORMATTER);
    }
}
