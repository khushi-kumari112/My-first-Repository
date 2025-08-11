package com.example.library.util;

import java.time.LocalDateTime;

/**
 * Provides current date-time for auditing issue and return operations.
 */
public final class DateTimeUtil {

    private DateTimeUtil() { }

    /**
     * Returns current system date-time. This method is used for both issuing and returning books.
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }
}