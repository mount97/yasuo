package com.yasuo.services.auth.impl;

import com.yasuo.constants.AuthConstants;
import com.yasuo.dtos.authentication.AuthenticationResponse;
import com.yasuo.dtos.authentication.LoginRequest;
import com.yasuo.models.User;
import com.yasuo.services.auth.AuthenticationService;
import com.yasuo.services.auth.JwtService;
import com.yasuo.services.auth.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final SecureRandom secureRandom = new SecureRandom();
    private final HttpServletResponse httpServletResponse;
    private final HttpServletRequest httpServletRequest;

    @Value("${cookie.same-site}")
    private String samSiteCookieValue;

    @Override
    public AuthenticationResponse login(LoginRequest request) throws NoSuchAlgorithmException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        var accessToken = createAccessTokenAndSetFingerprintCookie(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        httpServletResponse.addCookie(setRefreshTokenCookie(refreshToken));
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    @Override
    public AuthenticationResponse refreshToken(String userFingerprintHash) throws NoSuchAlgorithmException {
        if (StringUtils.isEmpty(userFingerprintHash)) {
            throw new IllegalArgumentException("userFingerprintHash cannot be null");
        }

        Cookie[] cookies = httpServletRequest.getCookies();
        Optional<Cookie> userFingerprintCookie = Arrays.stream(cookies).filter(c -> AuthConstants.FINGERPRINT_COOKIE_NAME.equalsIgnoreCase(c.getName())).findFirst();
        Optional<Cookie> refreshTokenCookie = Arrays.stream(cookies).filter(c -> AuthConstants.REFRESH_TOKEN_COOKIE_NAME.equalsIgnoreCase(c.getName())).findFirst();

        if(userFingerprintCookie.isEmpty() || ObjectUtils.isEmpty(userFingerprintCookie.get().getValue())) {
            logger.debug("Can not found userFingerprint cookie when refresh token");
            throw new IllegalArgumentException("Unable to refresh JWT token");
        }
        if(refreshTokenCookie.isEmpty() || ObjectUtils.isEmpty(refreshTokenCookie.get().getValue())) {
            logger.debug("Can not found refreshToken cookie when refresh token");
            throw new IllegalArgumentException("Unable to refresh JWT token");
        }
        String refreshToken = refreshTokenCookie.get().getValue();
        String userFingerprintInCookie = userFingerprintCookie.get().getValue();
        if(!userFingerprintHash.equals(convertToFingerprintHash(userFingerprintInCookie))) {
            logger.debug("userFingerprint incorrect");
            throw new IllegalArgumentException("Unable to refresh JWT token");
        }
        final String username;
        try {
            username = jwtService.extractUserName(refreshToken);
            if (StringUtils.isNotEmpty(username)) {
                UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);
                var newRefreshToken = jwtService.generateRefreshToken(userDetails);
                httpServletResponse.addCookie(setRefreshTokenCookie(newRefreshToken));
                if (jwtService.isTokenValid(refreshToken, userDetails)) {
                    var accessToken = createAccessTokenAndSetFingerprintCookie(userDetails);
                    return AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .build();
                }
            }
        } catch (Exception ex) {
            logger.error("Exception while processing refresh token: {}", ex.getMessage());
        }
        throw new IllegalArgumentException("Invalid refresh token.");
    }

    private String getFingerPrint() {
        byte[] randomFgp = new byte[50];
        secureRandom.nextBytes(randomFgp);
        return DatatypeConverter.printHexBinary(randomFgp);
    }

    private Cookie setFingerPrintCookie(String fingerprint) {
        Cookie cookie = new Cookie(AuthConstants.FINGERPRINT_COOKIE_NAME, fingerprint);
        cookie.setMaxAge(AuthConstants.FINGERPRINT_MAX_AGE);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setAttribute(AuthConstants.SAME_SITE, samSiteCookieValue);
        return cookie;
    }

    private Cookie setRefreshTokenCookie(String refreshToken) {
        Cookie cookie = new Cookie(AuthConstants.REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setAttribute(AuthConstants.SAME_SITE, samSiteCookieValue);
        return cookie;
    }

    private String createAccessTokenAndSetFingerprintCookie(UserDetails user) throws NoSuchAlgorithmException {
        Map<String, Object> jwtTokenExtraClaims = new HashMap<>();
        String userFingerprint = getFingerPrint();
        String userFingerprintHash = convertToFingerprintHash(userFingerprint);
        jwtTokenExtraClaims.put(AuthConstants.FINGERPRINT_CLAIM_NAME, userFingerprintHash);
        httpServletResponse.addCookie(setFingerPrintCookie(userFingerprint));
        return jwtService.generateToken(user, jwtTokenExtraClaims);
    }

    private String convertToFingerprintHash(String userFingerprint) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] userFingerprintDigest = digest.digest(userFingerprint.getBytes(StandardCharsets.UTF_8));
        return DatatypeConverter.printHexBinary(userFingerprintDigest);
    }
}
