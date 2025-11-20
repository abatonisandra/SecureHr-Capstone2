package com.securehr.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtHelper {

    public Algorithm createAlgorithm(String secret) {
        return Algorithm.HMAC256(secret);
    }

    public String buildToken(String subject, Date issuedAt, Date expiration, Algorithm algorithm) {
        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiration)
                .sign(algorithm);
    }

    public DecodedJWT parseToken(String token, Algorithm algorithm) {
        return JWT.require(algorithm)
                .build()
                .verify(token);
    }
}