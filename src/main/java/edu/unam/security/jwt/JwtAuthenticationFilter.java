package edu.unam.security.jwt;

import edu.unam.security.model.entity.RefreshToken;
import edu.unam.security.service.CustomUserDetailsService;
import edu.unam.security.service.JwtCookieService;
import edu.unam.security.service.RefreshTokenService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final JwtCookieService jwtCookieService;


    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService customUserDetailsService,
            RefreshTokenService refreshTokenService,
            JwtCookieService jwtCookieService) {

        this.jwtService =
                jwtService;

        this.customUserDetailsService =
                customUserDetailsService;

        this.refreshTokenService =
                refreshTokenService;

        this.jwtCookieService =
                jwtCookieService;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {


        String accessToken =
                obtenerAccessToken(request);

        // si hay atutentifi

        if (SecurityContextHolder
                .getContext()
                .getAuthentication() != null) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }


        // ACCESS TOKEN EXISTENTE
        if (StringUtils.hasText(accessToken)) {

            try {

                autenticarConAccessToken(
                        accessToken,
                        request
                );


            } catch (ExpiredJwtException e) {


                SecurityContextHolder
                        .clearContext();


                intentarRenovar(
                        request,
                        response
                );


            } catch (JwtException |
                     IllegalArgumentException e) {


                SecurityContextHolder
                        .clearContext();
            }

        } else {


            // =====================================
            // NO HAY ACCESS TOKEN
            // INTENTAR REFRESH
            // =====================================

            intentarRenovar(
                    request,
                    response
            );
        }


        filterChain.doFilter(
                request,
                response
        );
    }


    // =========================================
    // AUTENTICAR CON ACCESS TOKEN
    // =========================================

    private void autenticarConAccessToken(
            String token,
            HttpServletRequest request) {


        String username =
                jwtService.obtenerUsername(
                        token
                );


        if (!StringUtils.hasText(username)) {
            return;
        }


        UserDetails userDetails =
                customUserDetailsService
                        .loadUserByUsername(
                                username
                        );


        if (jwtService.esTokenValido(
                token,
                userDetails)) {


            establecerAuthentication(
                    userDetails,
                    request
            );
        }
    }


    // =========================================
    // REFRESH AUTOMÁTICO
    // =========================================

    private boolean intentarRenovar(
            HttpServletRequest request,
            HttpServletResponse response) {


        String token =
                obtenerCookie(
                        request,
                        JwtCookieService
                                .REFRESH_TOKEN_COOKIE
                );


        if (!StringUtils.hasText(token)) {
            return false;
        }


        Optional<RefreshToken> encontrado =
                refreshTokenService.validar(
                        token
                );


        if (encontrado.isEmpty()) {


            jwtCookieService.eliminarCookies(
                    response
            );


            return false;
        }


        RefreshToken refreshToken =
                encontrado.get();


        String email =
                refreshToken
                        .getUsuario()
                        .getEmail();


        UserDetails userDetails =
                customUserDetailsService
                        .loadUserByUsername(
                                email
                        );


        // =====================================
        // NUEVO ACCESS TOKEN
        // =====================================

        String nuevoAccessToken =
                jwtService.generarToken(
                        userDetails
                );


        // =====================================
        // ROTAR REFRESH TOKEN
        // =====================================

        RefreshToken nuevoRefreshToken =
                refreshTokenService.rotar(
                        refreshToken
                );


        // =====================================
        // ACTUALIZAR COOKIES
        // =====================================

        jwtCookieService.agregarAccessToken(
                response,
                nuevoAccessToken
        );


        jwtCookieService.agregarRefreshToken(
                response,
                nuevoRefreshToken.getToken()
        );


        // =====================================
        // AUTENTICAR PETICIÓN ACTUAL
        // =====================================

        establecerAuthentication(
                userDetails,
                request
        );


        return true;
    }


    // =========================================
    // SECURITY CONTEXT
    // =========================================

    private void establecerAuthentication(
            UserDetails userDetails,
            HttpServletRequest request) {


        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );


        authentication.setDetails(
                new WebAuthenticationDetailsSource()
                        .buildDetails(request)
        );


        SecurityContext context =
                SecurityContextHolder
                        .createEmptyContext();


        context.setAuthentication(
                authentication
        );


        SecurityContextHolder
                .setContext(
                        context
                );
    }


    // =========================================
    // OBTENER ACCESS TOKEN
    // =========================================

    private String obtenerAccessToken(
            HttpServletRequest request) {


        String authHeader =
                request.getHeader(
                        HttpHeaders.AUTHORIZATION
                );


        // Bearer para Postman/API
        if (StringUtils.hasText(authHeader)
                && authHeader.startsWith("Bearer ")) {

            return authHeader.substring(7);
        }


        // Cookie para navegador
        return obtenerCookie(
                request,
                JwtCookieService
                        .ACCESS_TOKEN_COOKIE
        );
    }


    // =========================================
    // OBTENER COOKIE
    // =========================================

    private String obtenerCookie(
            HttpServletRequest request,
            String nombre) {


        Cookie[] cookies =
                request.getCookies();


        if (cookies == null) {
            return null;
        }


        for (Cookie cookie : cookies) {

            if (nombre.equals(
                    cookie.getName())) {

                return cookie.getValue();
            }
        }


        return null;
    }


    // RUTAS QUE JWT NO DEBE PROCESAR

    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request) {


        String uri =
                request.getRequestURI();


        return uri.equals("/login")
                || uri.equals("/acceso-denegado")

                // no intentar refresh mientras cerramos sesión
                || uri.equals("/doLogout")

                || uri.startsWith("/api/auth/")

                || uri.startsWith("/css/")

                || uri.startsWith("/bootstrap/")

                || uri.startsWith("/tema/")

                || uri.startsWith("/image/")

                || uri.startsWith("/iconos/")

                || uri.equals("/favicon.ico")

                || uri.equals("/error")

                || uri.startsWith(
                "/.well-known/"
        );
    }
}