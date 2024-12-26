package com.shepherd.sheps_project.AppSecurity;

import com.shepherd.sheps_project.data.models.ShepsToken;
import com.shepherd.sheps_project.data.models.TokenType;
import com.shepherd.sheps_project.data.models.User;
import com.shepherd.sheps_project.data.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int BEARER_PREFIX_LENGTH = BEARER_PREFIX.length();

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
     String authHeader = request.getHeader(AUTHORIZATION);
     if(authHeader == null || !authHeader.startsWith(BEARER_PREFIX))
         return;
     String jwt = authHeader.substring(BEARER_PREFIX_LENGTH);
     processLogout(jwt);
    }

    private void processLogout(String jwt) {
        tokenRepository.findByToken(jwt)
                .ifPresent(this::invalidateUserTokens);
    }

    private void invalidateUserTokens(ShepsToken shepsToken) {
        User user = shepsToken.getUser();
        var tokens = tokenRepository.findAllByUserIdAndTokenType(user.getId(), TokenType.JWT);
        tokenRepository.deleteAll(tokens);
        SecurityContextHolder.clearContext();
    }
}