package com.shepherd.sheps_project.services.token;

import com.shepherd.sheps_project.data.models.ShepsToken;
import com.shepherd.sheps_project.data.models.TokenType;
import com.shepherd.sheps_project.data.models.User;
import com.shepherd.sheps_project.data.repository.TokenRepository;
import com.shepherd.sheps_project.utils.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService{
    private final TokenRepository tokenRepository;
    private static final int TOKEN_LENGTH = 100;
    @Override
    public String createToken(User user, TokenType tokenType, int expirationTimeInMinutes) {
        String token = RandomStringGenerator.generateRandomString(TOKEN_LENGTH);
        ShepsToken shepsToken = ShepsToken.builder()
                .user(user)
                .tokenType(tokenType)
                .token(token)
                .isExpired(false)
                .expirationTime(LocalDateTime.now().plusMinutes(expirationTimeInMinutes))
                .build();
        tokenRepository.save(shepsToken);
        return token;
    }

    @Override
    public ShepsToken createToken(ShepsToken shepsToken) {
        return null;
    }

    @Override
    public ShepsToken findByUserEmailTokenAndTokenType(String email, String token, TokenType tokenType) {
        return null;
    }
}




//    public Token createToken(User user, TokenType tokenType, Duration expiryDuration) {
//        String tokenValue = RandomStringGenerator.generateRandomString(128); // For simplicity
//
//        Token token = new Token();
//        token.setTokenValue(tokenValue);
//        token.setTokenType(tokenType);
//        token.setExpired(false);
//        token.setCreatedAt(LocalDateTime.now());
//        token.setExpiresAt(LocalDateTime.now().plus(expiryDuration));
//        token.setUser(user);
//
//        return tokenRepository.save(token);
//    }
//
//    public Optional<Token> validateToken(String tokenValue, TokenType tokenType) {
//        return tokenRepository.findByTokenValueAndTokenTypeAndExpiredFalse(tokenValue, tokenType)
//                .filter(token -> token.getExpiresAt().isAfter(LocalDateTime.now()));
//    }
//
//    public void expireToken(String tokenValue) {
//        tokenRepository.findByTokenValue(tokenValue).ifPresent(token -> {
//            token.setExpired(true);
//            tokenRepository.save(token);
//        });
//    }
//
//    public void expireAllTokensForUser(User user, TokenType tokenType) {
//        List<Token> tokens = tokenRepository.findAllByUserAndTokenType(user, tokenType);
//        tokens.forEach(token -> token.setExpired(true));
//        tokenRepository.saveAll(tokens);
//    }
//}