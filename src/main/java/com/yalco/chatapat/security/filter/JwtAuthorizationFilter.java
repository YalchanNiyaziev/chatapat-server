package com.yalco.chatapat.security.filter;


import com.yalco.chatapat.security.jwt.JwtConfiguration;
import com.yalco.chatapat.security.jwt.JwtProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static java.util.Optional.ofNullable;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtConfiguration jwtConfig;
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthorizationFilter(JwtConfiguration jwtConfig, JwtProvider jwtProvider, UserDetailsService userDetailsService) {
        this.jwtConfig = jwtConfig;
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getJwtFromRequest(httpServletRequest);
        if(StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
            String username = jwtProvider.getUsernameFromToken(jwt);
            UserDetails user = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null,
                            ofNullable(user).map(UserDetails::getAuthorities).orElse(new ArrayList<>()));

                    //TODO research it
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
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
