package com.shepherd.sheps_project.security;

import com.shepherd.sheps_project.data.models.ShepsToken;
import com.shepherd.sheps_project.data.models.TokenType;
import com.shepherd.sheps_project.data.models.User;
import com.shepherd.sheps_project.data.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int BEARER_PREFIX_LENGTH = BEARER_PREFIX.length();

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("::::: Initiating logout process :::::");
        String authHeader = request.getHeader(AUTHORIZATION);
        if(authHeader == null || !authHeader.startsWith(BEARER_PREFIX))
            return;
        String jwt = authHeader.substring(BEARER_PREFIX_LENGTH);
        processLogout(jwt);
    }

    private void processLogout(String jwt) {
        log.info("::::: Processing logout :::::");
        tokenRepository.findByTokenAndTokenType(jwt, TokenType.JWT)
                .ifPresentOrElse(this::invalidateUserTokens,
                        ()-> log.warn("::::: No matching token found for invalidation :::::"));
    }

    private void invalidateUserTokens(ShepsToken shepsToken) {
        log.info("::::: Initiating user jwt tokens invalidation :::::");
        User user = shepsToken.getUser();
        var tokens = tokenRepository.findAllByUserIdAndTokenType(user.getId(), TokenType.JWT);
        if (!tokens.isEmpty()) {
            tokenRepository.deleteAll(tokens);
            log.info("::::: Invalidated {} JWT tokens :::::", tokens.size());
        } else {
            log.warn("::::: No JWT tokens found for invalidation :::::");
        }
        SecurityContextHolder.clearContext();
    }
}