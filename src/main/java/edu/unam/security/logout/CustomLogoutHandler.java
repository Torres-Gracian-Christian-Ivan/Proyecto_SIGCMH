package edu.unam.security.logout;

import edu.unam.security.service.JwtCookieService;
import edu.unam.security.service.RefreshTokenService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import org.springframework.stereotype.Component;


@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final RefreshTokenService refreshTokenService;
    private final JwtCookieService jwtCookieService;


    public CustomLogoutHandler(
            RefreshTokenService refreshTokenService,
            JwtCookieService jwtCookieService) {

        this.refreshTokenService =
                refreshTokenService;

        this.jwtCookieService =
                jwtCookieService;
    }


    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {

        String refreshToken =
                obtenerRefreshToken(request);


        // =========================================
        // REVOCAR REFRESH TOKEN
        // =========================================

        if (refreshToken != null
                && !refreshToken.isBlank()) {

            refreshTokenService.revocar(
                    refreshToken
            );
        }


        // =========================================
        // ELIMINAR COOKIES JWT
        // =========================================

        jwtCookieService.eliminarCookies(
                response
        );
    }


    private String obtenerRefreshToken(
            HttpServletRequest request) {

        Cookie[] cookies =
                request.getCookies();


        if (cookies == null) {
            return null;
        }


        for (Cookie cookie : cookies) {

            if (JwtCookieService
                    .REFRESH_TOKEN_COOKIE
                    .equals(cookie.getName())) {

                return cookie.getValue();
            }
        }


        return null;
    }
}