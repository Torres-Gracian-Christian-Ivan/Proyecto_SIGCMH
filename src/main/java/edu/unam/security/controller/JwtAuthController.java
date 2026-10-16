package edu.unam.security.controller;

import edu.unam.security.dto.AuthResponse;
import edu.unam.security.jwt.JwtService;
import edu.unam.security.model.entity.RefreshToken;
import edu.unam.security.request.LoginRequest;
import edu.unam.security.request.RefreshTokenRequest;
import edu.unam.security.service.CustomUserDetailsService;
import edu.unam.security.service.JwtCookieService;
import edu.unam.security.service.RefreshTokenService;
import jakarta.servlet.http.Cookie;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
public class JwtAuthController {


    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtCookieService jwtCookieService;


    public JwtAuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            RefreshTokenService refreshTokenService,
            CustomUserDetailsService customUserDetailsService, JwtCookieService jwtCookieService) {

        this.authenticationManager =
                authenticationManager;

        this.jwtService =
                jwtService;

        this.refreshTokenService =
                refreshTokenService;

        this.customUserDetailsService =
                customUserDetailsService;

        this.jwtCookieService =
                jwtCookieService;
    }


    /*@PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request) { //antes del refrsh

     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response) {


        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );


        UserDetails userDetails =
                (UserDetails)
                        authentication.getPrincipal();


        String accessToken =
                jwtService.generarToken(
                        userDetails
                );


        RefreshToken refreshToken =
                refreshTokenService
                        .crearOReemplazar(
                                userDetails.getUsername()
                        );

        jwtCookieService.agregarAccessToken(
                response,
                accessToken
        );

        jwtCookieService.agregarRefreshToken(
                response,
                refreshToken.getToken()
        );


        return ResponseEntity.ok(
                new AuthResponse(
                        accessToken,
                        refreshToken.getToken(),
                        "Bearer"
                )
        );
    }

//refresh
    private String obtenerRefreshTokenDesdeCookie(
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

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            HttpServletRequest request,
            HttpServletResponse response) {

        String token =
                obtenerRefreshTokenDesdeCookie(request);


        if (token == null || token.isBlank()) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            Map.of(
                                    "status", 401,
                                    "error",
                                    "Refresh Token no encontrado"
                            )
                    );
        }


        Optional<RefreshToken> encontrado =
                refreshTokenService.validar(token);


        if (encontrado.isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            Map.of(
                                    "status", 401,
                                    "error",
                                    "Refresh Token inválido o expirado"
                            )
                    );
        }


        RefreshToken refreshToken =
                encontrado.get();


        String email =
                refreshToken
                        .getUsuario()
                        .getEmail();


        UserDetails userDetails =
                customUserDetailsService
                        .loadUserByUsername(email);


        // Nuevo Access Token
        String nuevoAccessToken =
                jwtService.generarToken(userDetails);


        // Rotar Refresh Token
        RefreshToken nuevoRefreshToken =
                refreshTokenService.rotar(
                        refreshToken
                );


        // Actualizar ambas cookies
        jwtCookieService.agregarAccessToken(
                response,
                nuevoAccessToken
        );

        jwtCookieService.agregarRefreshToken(
                response,
                nuevoRefreshToken.getToken()
        );


        return ResponseEntity.ok(
                new AuthResponse(
                        nuevoAccessToken,
                        nuevoRefreshToken.getToken(),
                        "Bearer"
                )
        );
    }
}