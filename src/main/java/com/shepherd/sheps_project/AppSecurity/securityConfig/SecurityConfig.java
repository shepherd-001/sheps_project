package com.shepherd.sheps_project.AppSecurity.securityConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shepherd.sheps_project.AppSecurity.AllowedURIs;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.Key;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${jwt_secret_key}")
    private String jwtSecretKey;
//    private ValidationMessage() {
//        throw new UnsupportedOperationException("Utility class cannot be instantiated");
//    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Key signInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
////
//@Bean
//public SecurityFilterChain securityFilterChain(HttpSecurity http, BlackListedTokenManager blackListedTokenManager) throws Exception {
//    http.cors()
//            .and()
//            .csrf(AbstractHttpConfigurer::disable)
//            .addFilterBefore(new CustomAuthorizationFilter(blackListedTokenManager,new ObjectMapper()), UsernamePasswordAuthenticationFilter.class)
//            .authorizeHttpRequests(authorize -> authorize
//                    .requestMatchers(WhiteList.patterns).permitAll()
//                    .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
//                    .requestMatchers(HttpMethod.POST, "/keycloak/auth/**").permitAll()
//                    .anyRequest().authenticated())
//            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter)))
//            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            )
//            .httpBasic(AbstractHttpConfigurer::disable);
//    return http.build();
//}
//@Bean
//CorsConfigurationSource corsConfigurationSource() {
//
//    CorsConfiguration configuration = new CorsConfiguration();
////        configuration.setAllowedOrigins(Arrays.asList(AllowedHost.patterns));
//    configuration.setAllowedOrigins(Arrays.asList(allowedHost.getPatterns()));
//    configuration.setAllowedMethods(Arrays.asList(allowedHost.getMethods()));
//    configuration.setAllowCredentials(true);
//    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Requestor-Type", "Origin", "X-Requested-With", "Accept",  "Content-Type", "Cache-Control"));
//    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//    source.registerCorsConfiguration("/**", configuration);
//    return source;
//}
//