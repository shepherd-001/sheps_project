package com.shepherd.sheps_project.utils;

public final class RegexPattern {
    public static final String EMAIL = "^(?=.{1,254}$)(?!.*\\.\\.)(?!\\.)(?!.*\\.$)(?!.*@[^a-zA-Z0-9.-])(?!.*@.*\\.\\-)(?!.*@.*-\\.)[a-zA-Z0-9._%+-]{1,64}@[a-zA-Z0-9.-]{1,255}\\.[a-zA-Z]{2,}$";
    public static final String PASSWORD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*().?]).{8,20}$";
    public static final String USER_NAME = "^[A-Za-z]+(?:[-'][A-Za-z]+)?$";


    private RegexPattern() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
