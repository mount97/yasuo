package com.yasuo.services.auth.impl;

import com.yasuo.dtos.authentication.AuthenticationResponse;
import com.yasuo.dtos.authentication.LoginRequest;
import com.yasuo.models.User;
import com.yasuo.services.auth.AuthenticationService;
import com.yasuo.services.auth.JwtService;
import com.yasuo.services.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .build();
    }

    @Override
    public AuthenticationResponse refreshToken(String refreshToken) {
        if (StringUtils.isEmpty(refreshToken)) {
            throw new IllegalArgumentException("refreshToken cannot be null");
        }
        final String username;
        try {
            username = jwtService.extractUserName(refreshToken);
            if (StringUtils.isNotEmpty(username)) {
                UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);
                var newRefreshToken = jwtService.generateRefreshToken(userDetails);
                if (jwtService.isTokenValid(refreshToken, userDetails)) {
                    var accessToken = jwtService.generateToken(userDetails);
                    return AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(newRefreshToken)
                            .username(username)
                            .build();
                }
            }
        } catch (Exception ex) {
            logger.error("Exception while processing refresh token: {}", ex.getMessage());
        }
        throw new IllegalArgumentException("Invalid refresh token.");
    }
}
