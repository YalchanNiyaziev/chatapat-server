package com.yalco.chatapat.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class JwtConfiguration {

    @Value("${authorization.jwt.secret.key}")
    private String secret;

    @Value("${authorization.jwt.token.prefix}")
    private String tokenPrefix;

    @Value("${authorization.jwt.expiration.seconds}")
    private Long expirationInSeconds;


    public String getSecret() {
        return secret;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public Long getExpirationInSeconds() {
        return expirationInSeconds;
    }

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

    public String getAuthorizationHeaderPrefixDelimeter() {
        return " ";
    }
}
