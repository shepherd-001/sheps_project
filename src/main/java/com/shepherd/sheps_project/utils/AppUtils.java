package com.shepherd.sheps_project.utils;

import com.shepherd.sheps_project.data.models.User;
import com.shepherd.sheps_project.exceptions.ShepsException;
import com.shepherd.sheps_project.security.AuthenticatedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public static Pageable createPageRequest(int pageNumber, int itemsPerPage, String sortField, Sort.Direction direction) {
        pageNumber = Math.max(pageNumber - 1, 0);
        return PageRequest.of(pageNumber, itemsPerPage, Sort.by(direction, sortField));
    }

    public static String customAuthResponse(String message, LocalDateTime timestamp, boolean isSuccessful){
        String formattedTimestamp =
        timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss, dd-MM-yyyy"));
        return String.format("{\"message\": \"%s\", \"timestamp\": \"%s\", \"isSuccessful\": \"%b\"}", message, formattedTimestamp, isSuccessful);
    }

    private AppUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
