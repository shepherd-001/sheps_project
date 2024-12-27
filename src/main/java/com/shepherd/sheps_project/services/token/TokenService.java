package com.shepherd.sheps_project.services.token;

import com.shepherd.sheps_project.data.models.ShepsToken;
import com.shepherd.sheps_project.data.models.TokenType;
import com.shepherd.sheps_project.data.models.User;


public interface TokenService {
    String saveToken(User user, TokenType tokenType, int expirationTimeInMinutes);
    void saveToken(ShepsToken shepsToken);
    ShepsToken validateToken(String token, String email, TokenType tokenType);
    void deleteToken(ShepsToken shepsToken);
}
