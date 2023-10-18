package com.yasuo.services.auth.impl;

import com.yasuo.dtos.authentication.AuthenticationResponse;
import com.yasuo.dtos.authentication.LoginRequest;
import com.yasuo.repository.UserRepository;
import com.yasuo.services.auth.AuthenticationService;
import com.yasuo.services.auth.JwtService;
import com.yasuo.services.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));
        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .build();
    }

    @Override
    public AuthenticationResponse refreshToken(String authHeader) {
        final String refreshToken;
        final String username;
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header.");
        }
        try {
            refreshToken = authHeader.substring(7);
            username = jwtService.extractUserName(refreshToken);
            if (StringUtils.isNotEmpty(username)) {
                UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);
                if (jwtService.isTokenValid(refreshToken, userDetails)) {
                    var accessToken = jwtService.generateToken(userDetails);
                    return AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .username(username)
                            .build();
                }
            }
        } catch (Exception ex) {
            logger.error("Invalid refresh token.");
        }
        throw new UsernameNotFoundException("Invalid refresh token.");
    }
}
