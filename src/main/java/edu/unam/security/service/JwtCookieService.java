package edu.unam.security.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class JwtCookieService {

    public static final String ACCESS_TOKEN_COOKIE =
            "access_token";

    public static final String REFRESH_TOKEN_COOKIE =
            "refresh_token";


    @Value("${security.jwt.expiration}")
    private long accessExpiration;


    @Value("${security.jwt.refresh-expiration}")
    private long refreshExpiration;


    public void agregarAccessToken(
            HttpServletResponse response,
            String token) {

        ResponseCookie cookie =
                ResponseCookie
                        .from(
                                ACCESS_TOKEN_COOKIE,
                                token
                        )
                        .httpOnly(true)
                        .secure(false)
                        .sameSite("Lax")
                        .path("/")
                        .maxAge(
                                Duration.ofMillis(
                                        accessExpiration
                                )
                        )
                        .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );
    }


    public void agregarRefreshToken(
            HttpServletResponse response,
            String token) {

        ResponseCookie cookie =
                ResponseCookie
                        .from(
                                REFRESH_TOKEN_COOKIE,
                                token
                        )
                        .httpOnly(true)
                        .secure(false)
                        .sameSite("Lax")
                        .path("/")
                        .maxAge(
                                Duration.ofMillis(
                                        refreshExpiration
                                )
                        )
                        .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );
    }


    public void eliminarCookies(
            HttpServletResponse response) {

        ResponseCookie accessCookie =
                ResponseCookie
                        .from(
                                ACCESS_TOKEN_COOKIE,
                                ""
                        )
                        .httpOnly(true)
                        .secure(false)
                        .sameSite("Lax")
                        .path("/")
                        .maxAge(Duration.ZERO)
                        .build();


        ResponseCookie refreshCookie =
                ResponseCookie
                        .from(
                                REFRESH_TOKEN_COOKIE,
                                ""
                        )
                        .httpOnly(true)
                        .secure(false)
                        .sameSite("Lax")
                        .path("/")
                        .maxAge(Duration.ZERO)
                        .build();


        response.addHeader(
                HttpHeaders.SET_COOKIE,
                accessCookie.toString()
        );

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                refreshCookie.toString()
        );
    }
}