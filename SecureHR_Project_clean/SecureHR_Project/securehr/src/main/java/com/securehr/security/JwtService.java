package com.securehr.security;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private final Algorithm algorithm;
    private final JwtHelper jwtHelper;

    public JwtService(JwtProperties jwtProperties, JwtHelper jwtHelper) {
        this.jwtProperties = jwtProperties;
        this.jwtHelper = jwtHelper;
        this.algorithm = jwtHelper.createAlgorithm(jwtProperties.getSecret());
    }

    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getExpiration());
        return jwtHelper.buildToken(userDetails.getUsername(), now, expiry, algorithm);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        DecodedJWT jwt = jwtHelper.parseToken(token, algorithm);
        return jwt.getSubject();
    }

    public Date extractExpiration(String token) {
        DecodedJWT jwt = jwtHelper.parseToken(token, algorithm);
        return jwt.getExpiresAt();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}

