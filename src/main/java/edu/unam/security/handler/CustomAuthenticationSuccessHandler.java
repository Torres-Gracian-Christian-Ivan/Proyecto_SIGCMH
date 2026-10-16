package edu.unam.security.handler;

import edu.unam.security.jwt.JwtService;
import edu.unam.security.model.entity.RefreshToken;
import edu.unam.security.service.JwtCookieService;
import edu.unam.security.service.RefreshTokenService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class CustomAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final JwtCookieService jwtCookieService;


    public CustomAuthenticationSuccessHandler(
            JwtService jwtService,
            RefreshTokenService refreshTokenService,
            JwtCookieService jwtCookieService) {

        this.jwtService =
                jwtService;

        this.refreshTokenService =
                refreshTokenService;

        this.jwtCookieService =
                jwtCookieService;
    }


    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {


        UserDetails userDetails =
                (UserDetails)
                        authentication.getPrincipal();


        // acces token
        String accessToken =
                jwtService.generarToken(
                        userDetails
                );

        // refresh
        RefreshToken refreshToken =
                refreshTokenService
                        .crearOReemplazar(
                                userDetails.getUsername()
                        );

        // COOKIES
        jwtCookieService.agregarAccessToken(
                response,
                accessToken
        );


        jwtCookieService.agregarRefreshToken(
                response,
                refreshToken.getToken()
        );

        //redirec
        response.sendRedirect("/");
    }
}