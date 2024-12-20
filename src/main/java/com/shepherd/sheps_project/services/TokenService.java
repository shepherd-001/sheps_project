package com.shepherd.sheps_project.services;

import com.shepherd.sheps_project.data.models.ShepsToken;
import com.shepherd.sheps_project.data.models.TokenType;
import com.shepherd.sheps_project.data.models.User;

import java.time.Duration;

public interface TokenService {
    ShepsToken createToken(User user, TokenType tokenType, Duration expiryDuration);
    ShepsToken findByUserEmailTokenAndTokenType(String email, String token, TokenType tokenType);

}

//@Service
//public class TokenService {
//
//    @Autowired
//    private TokenRepository tokenRepository;
//
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