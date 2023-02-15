package com.yusufislamdemir.creditapplicationsystem.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class TokenGenerator {

    @Value("${jwt-variables.KEY}")
    private String key;
    @Value("${jwt-variables.ISSUER}")
    private String issuer;
    @Value("${jwt-variables.EXPIRES_ACCESS_TOKEN_MINUTE}")
    private long expires_access_token_minute;

    public String generate(Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + expires_access_token_minute * 60 * 1000))
                .withIssuer(issuer)
                .sign(Algorithm.HMAC256(key.getBytes()));
    }

    public DecodedJWT verifyJWT(String token) {
        Algorithm algorithm = Algorithm.HMAC256(key.getBytes());
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            return jwtVerifier.verify(token);
        } catch (Exception e) {
            throw e;
        }
    }
}

