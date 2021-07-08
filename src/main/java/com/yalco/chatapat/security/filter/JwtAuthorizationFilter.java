package com.yalco.chatapat.security.filter;


import com.yalco.chatapat.security.jwt.JwtConfiguration;
import com.yalco.chatapat.security.jwt.JwtProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtConfiguration jwtConfig;
    private final JwtProvider jwtProvider;

    public JwtAuthorizationFilter(JwtConfiguration jwtConfig, JwtProvider jwtProvider) {
        this.jwtConfig = jwtConfig;
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getJwtFromRequest(httpServletRequest);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String tokenPrefix = jwtConfig.getTokenPrefix();
        String bearerToken = request.getHeader(jwtConfig.getAuthorizationHeader());
        if(tokenPrefix != null && StringUtils.hasText(bearerToken) && bearerToken.startsWith(tokenPrefix)) {
            return bearerToken.substring(bearerToken.indexOf(jwtConfig.getAuthorizationHeaderPrefixDelimeter()));
        }
        return null;
    }
}
