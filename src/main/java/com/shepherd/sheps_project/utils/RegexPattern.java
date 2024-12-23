package com.shepherd.sheps_project.utils;

public class RegexPattern {
    public static final String EMAIL = "^[a-zA-Z0-9](\\.?[a-zA-Z0-9_-])*@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,}$";
    public static final String PASSWORD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@#$%^&*()_+={}\\[\\];':<>,.?/\\\\|\"]).{8,16}$";
    public static final String USER_NAME = "^[a-zA-Z]+([-'a-zA-Z]*[a-zA-Z])?$";



    private RegexPattern() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
