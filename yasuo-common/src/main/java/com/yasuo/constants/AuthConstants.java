package com.yasuo.constants;

public class AuthConstants {
    private AuthConstants() {
    }

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer";
    public static final String UNAUTHORIZED_ERROR_MESSAGE = "Full authentication is required to access this resource";
    public static final String COMMON_ERROR_MESSAGE = "An error occurred while processing request";
    public static final String BAD_CREDENTIALS_ERROR_MESSAGE = "Invalid username or password";
    public static final String FINGERPRINT_COOKIE_NAME = "__Secure-Fgp";
    public static final Integer FINGERPRINT_MAX_AGE  = 60 * 60 * 8;
    public static final String REFRESH_TOKEN_COOKIE_NAME = "__Refresh-tk";
    public static final String SAME_SITE  = "SameSite";
    public static final String FINGERPRINT_CLAIM_NAME  = "fgp";
}
