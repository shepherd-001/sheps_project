package com.shepherd.sheps_project.data.repository;

import com.shepherd.sheps_project.data.models.ShepsToken;
import com.shepherd.sheps_project.data.models.TokenType;
import com.shepherd.sheps_project.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<ShepsToken, UUID> {
    @Query("""
           select t from ShepsToken t
           where t.user.email = :email and t.token = :token
           and t.tokenType = :tokenType\s
          \s""")
    Optional<ShepsToken> findByUserAndTokenAndTokenType(@Param("email") String email,
         @Param("token") String token, @Param("tokenType") TokenType tokenType);
    Optional<ShepsToken> findByTokenAndTokenTypeAndExpiredFalse(String token, TokenType type);
    Optional<ShepsToken> findByToken(String token);
    List<ShepsToken> findAllByUserAndTokenType(User user, TokenType tokenType);
    void deleteByToken(String token);
}
