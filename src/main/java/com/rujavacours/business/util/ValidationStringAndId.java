package com.rujavacours.business.util;

/**
 * Валидатор Строк и ID
 *
 * @author Viktor Kuldorov
 */
public class ValidationStringAndId {
    public static boolean isNoValidId(Long id) {
        return id != null && id != 0;
    }

    public static boolean isValidId(Long id) {
        return id == null || id == 0;
    }

    public static boolean isValidString(String title) {
        return title == null || title.trim().length() == 0;
    }
}
