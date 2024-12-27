package com.shepherd.sheps_project.utils;

import org.apache.commons.lang3.RandomStringUtils;


public final class RandomStringGenerator {
    public static String generateRandomString(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    private RandomStringGenerator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}


