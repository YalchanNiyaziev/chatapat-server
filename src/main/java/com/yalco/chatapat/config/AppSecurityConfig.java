package com.yalco.chatapat.config;

import com.yalco.chatapat.security.filter.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationEntryPoint entryPoint;
    private final JwtAuthorizationFilter authorizationFilter;

    public AppSecurityConfig(AuthenticationEntryPoint entryPoint, JwtAuthorizationFilter authorizationFilter) {
        this.entryPoint = entryPoint;
        this.authorizationFilter = authorizationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //TODO enable them
        http.cors().disable();
        http.csrf().disable();

        http.authorizeRequests().mvcMatchers("/api/user-management/users").authenticated()
                .and().authorizeRequests().mvcMatchers("/api/auth/login").permitAll();

        http.exceptionHandling().authenticationEntryPoint(entryPoint);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
