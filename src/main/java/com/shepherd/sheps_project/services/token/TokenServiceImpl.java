package com.shepherd.sheps_project.services.token;

import com.shepherd.sheps_project.data.models.ShepsToken;
import com.shepherd.sheps_project.data.models.TokenType;
import com.shepherd.sheps_project.data.models.User;
import com.shepherd.sheps_project.data.repository.TokenRepository;
import com.shepherd.sheps_project.exceptions.ShepsTokenException;
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
        log.info("Created a new token");
        return token;
    }

    @Override
    public ShepsToken createToken(ShepsToken shepsToken) {
        return null;
    }

    @Override
    public ShepsToken validateToken(String token, String email, TokenType tokenType) {
        if(token.isBlank())
            throw new ShepsTokenException("Token is required");
        if(email.isBlank())
            throw new ShepsTokenException("Email address is required");

        ShepsToken shepsToken = tokenRepository.findByUserAndTokenAndTokenType(email, token, tokenType);
        if(shepsToken == null){
            log.info("Token not found");
            throw new ShepsTokenException("Token is invalid or expired");
        }else if(shepsToken.getExpirationTime().isBefore(LocalDateTime.now())){
            log.info("Token is expired");
            throw new ShepsTokenException("Token is expired");
        }
        return shepsToken;
    }

    @Override
    public void deleteToken(ShepsToken shepsToken) {
        tokenRepository.delete(shepsToken);
        log.info("Deleted a token");
    }
}

