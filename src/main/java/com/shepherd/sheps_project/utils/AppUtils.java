package com.shepherd.sheps_project.utils;

import com.shepherd.sheps_project.data.models.User;
import com.shepherd.sheps_project.exceptions.ShepsException;
import com.shepherd.sheps_project.security.AuthenticatedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public final class AppUtils {
    public static User getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser)) {
                throw new ShepsException("No authenticated user found");
            }
            return ((AuthenticatedUser) authentication.getPrincipal()).getUser();
        } catch (Exception e) {
            log.error("Error fetching authenticated user: {}", e.getMessage());
            throw new ShepsException("Failed to fetch authenticated user");
        }
    }

    private AppUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
