package com.example.project.config;

import com.example.project.filter.JWTRequestFilter;
import com.example.project.filter.UserAuthenticationFilter;
import com.example.project.utils.JWTUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UserAuthenticationFilter userAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtils jwtUtils) {
        UserAuthenticationFilter userAuthenticationFilter = new UserAuthenticationFilter(jwtUtils);
        userAuthenticationFilter.setRequiresAuthenticationRequestMatcher(AntPathRequestMatcher.antMatcher(POST, "/api/v1.0/login"));
        userAuthenticationFilter.setAuthenticationManager(authenticationManager);
        return userAuthenticationFilter;
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserAuthenticationFilter filter,
                                                   UserDetailsService userDetailsService,
                                                   JWTUtils jwtUtils
    ) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(
                                "/api/v1.0/register",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().authenticated())
                .addFilter(filter)
                .addFilterAfter(new JWTRequestFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(userDetailsService);
        return http.build();
    }
}

