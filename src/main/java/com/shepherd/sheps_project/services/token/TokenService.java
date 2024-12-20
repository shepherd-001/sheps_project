package com.shepherd.sheps_project.services.token;

import com.shepherd.sheps_project.data.models.ShepsToken;
import com.shepherd.sheps_project.data.models.TokenType;
import com.shepherd.sheps_project.data.models.User;


public interface TokenService {
    String createToken(User user, TokenType tokenType, int expirationTimeInMinutes);
    ShepsToken validateToken(String token, String email, TokenType tokenType);
    ShepsToken createToken(ShepsToken shepsToken);
    ShepsToken findByUserEmailTokenAndTokenType(String email, String token, TokenType tokenType);
    void deleteToken(ShepsToken shepsToken);
}
