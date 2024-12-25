package com.example.project.filter;

import com.example.project.models.UserDetailsEntity;
import com.example.project.utils.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class UserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JWTUtils jwtUtils;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        String email = "";
        String password = "";

        try (ServletInputStream inputStream = request.getInputStream()) {
            var loginData = new ObjectMapper().readValue(inputStream, Map.class);
            email = (String) loginData.get("email");
            password = (String) loginData.get("password");
        } catch (IOException ex) {
            log.error("Invalid login request", ex);
        }

        var token = new UsernamePasswordAuthenticationToken(email, password);
        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) throws IOException {
        var user = (UserDetailsEntity) authentication.getPrincipal();

        String accessToken = jwtUtils.generateAccessToken(user);

        Map<String, String> token = new HashMap<>();
        token.put("access_token", accessToken);

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(APPLICATION_JSON_VALUE);

        Map<String, String> error = new HashMap<>();
        error.put("error_message", failed.getMessage());

        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}
