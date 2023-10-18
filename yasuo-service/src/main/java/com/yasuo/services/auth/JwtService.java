package com.yasuo.services.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtService {
    String extractUserName(String token);

    String generateToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);
}
