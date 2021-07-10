package com.yalco.chatapat.security.jwt;

import com.yalco.chatapat.security.JpaUserDetails;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    private final JwtConfiguration jwtConfig;

    public JwtProvider(JwtConfiguration jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(Authentication authentication) {
        JpaUserDetails principal = (JpaUserDetails) authentication.getPrincipal();
        Long timeToLive = jwtConfig.getExpirationInSeconds();
        if (timeToLive == null) {
            logger.error("JWT expiration not set");
            return null;
        }

        Date createdAt = new Date();
        Date expirationDate = new Date(createdAt.getTime() + timeToLive);

        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(createdAt)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512,
                        jwtConfig.getSecret())
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtConfig.getSecret())
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            logger.warn("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.warn("Invalid JWT");
        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            logger.warn("Expired JWT");
        } catch (UnsupportedJwtException ex) {
            logger.warn("Unsupported JWT");
        } catch (IllegalArgumentException ex) {
            logger.warn("JWT claims string is empty.");
        }

        return false;
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return  claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtConfig.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }
}
