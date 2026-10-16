package edu.unam.security;

import edu.unam.security.handler.CustomAccessDeniedHandler;
import edu.unam.security.handler.CustomAuthenticationSuccessHandler;
import edu.unam.security.jwt.JwtAuthenticationFilter;
import edu.unam.security.logout.CustomLogoutHandler;
import edu.unam.security.service.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomLogoutHandler customLogoutHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    
    public SecurityConfiguration(
            CustomUserDetailsService customUserDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
            CustomLogoutHandler customLogoutHandler, CustomAccessDeniedHandler customAccessDeniedHandler) {

        this.customUserDetailsService =
                customUserDetailsService;

        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;

        this.customAuthenticationSuccessHandler =
                customAuthenticationSuccessHandler;

        this.customLogoutHandler =
                customLogoutHandler;

        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                
                .csrf(csrf ->
                        csrf.ignoringRequestMatchers(
                                "/api/**"
                        )
                )
                
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )


                // No guardar requests protegidos en sesión
                .requestCache(cache ->
                        cache.disable()
                )


                
                // Autorizacion
                .authorizeHttpRequests(auth -> auth
                        // publico
                        .requestMatchers(
                                "/login",
                                "/acceso-denegado",
                                "/api/auth/**",
                                "/css/**",
                                "/bootstrap/**",
                                "/tema/**",
                                "/image/**",
                                "/iconos/**",
                                "/favicon.ico",
                                "/error"
                        ).permitAll()
                        
                        // USUARIOS
                        // SOLO ADMIN
                        .requestMatchers(
                                "/usuarios/**",
                                "/api/usuarios/**"
                        ).hasRole(
                                "ADMINISTRADOR"
                        )
                        
                        // MÉDICOS WEB
                        // ADMIN PUEDE MODIFICAR

                        .requestMatchers(
                                "/medicos/nuevo",
                                "/medicos/editar/**",
                                "/medicos/eliminar/**"
                        ).hasRole(
                                "ADMINISTRADOR"
                        )


                        .requestMatchers(
                                HttpMethod.POST,
                                "/medicos/**"
                        ).hasRole(
                                "ADMINISTRADOR"
                        )
                        
                        // MÉDICOS API
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/medicos/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "RECEPCIONISTA"
                        )


                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/medicos/**"
                        ).hasRole(
                                "ADMINISTRADOR"
                        )


                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/medicos/**"
                        ).hasRole(
                                "ADMINISTRADOR"
                        )


                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/medicos/**"
                        ).hasRole(
                                "ADMINISTRADOR"
                        )


                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/medicos/**"
                        ).hasRole(
                                "ADMINISTRADOR"
                        )
                        
                        // HORARIOS WEB
                        .requestMatchers(
                                HttpMethod.POST,
                                "/medicos/*/horarios/**"
                        ).hasRole(
                                "ADMINISTRADOR"
                        )


                        .requestMatchers(
                                HttpMethod.GET,
                                "/medicos/*/horarios/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "RECEPCIONISTA"
                        )
                        
                        // HORARIOS API
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/medicos/*/horarios/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "RECEPCIONISTA"
                        )


                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/medicos/*/horarios/**"
                        ).hasRole(
                                "ADMINISTRADOR"
                        )


                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/medicos/*/horarios/**"
                        ).hasRole(
                                "ADMINISTRADOR"
                        )


                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/medicos/*/horarios/**"
                        ).hasRole(
                                "ADMINISTRADOR"
                        )
                        
                        // PACIENTES
                        .requestMatchers(
                                "/pacientes/**",
                                "/api/pacientes/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "RECEPCIONISTA"
                        )
                         
                        // CITAS
                        .requestMatchers(
                                "/citas/**",
                                "/api/citas/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "RECEPCIONISTA"
                        )
                        
                        // MÉDICOS - CONSULTA WEB
                        .requestMatchers(
                                "/medicos/**",
                                "/horarios/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "RECEPCIONISTA"
                        )
                        
                        // REPORTES
                        .requestMatchers(
                                "/reportes/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "RECEPCIONISTA"
                        )
                        
                        // TODO LO DEMÁS
                        .anyRequest()
                        .authenticated()
                )
                .exceptionHandling(exception ->
                        exception.accessDeniedHandler(
                                customAccessDeniedHandler
                        )
                )

                // PROVIDER
                .authenticationProvider(
                        authenticationProvider()
                )

                // FILTRO JWT
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                // LOGIN
                .formLogin(form -> form

                        .loginPage(
                                "/login"
                        )

                        .usernameParameter(
                                "email"
                        )

                        .passwordParameter(
                                "password"
                        )

                        .successHandler(
                                customAuthenticationSuccessHandler
                        )

                        .failureUrl(
                                "/login?error"
                        )

                        .permitAll()
                )

                // LOGOUT
                .logout(logout -> logout

                        .logoutUrl(
                                "/doLogout"
                        )

                        .addLogoutHandler(
                                customLogoutHandler
                        )

                        .logoutSuccessUrl(
                                "/login?logout"
                        )

                        .clearAuthentication(
                                true
                        )

                        .permitAll()
                );


        return http.build();
    }

    // PASSWORD ENCODER
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // AUTHENTICATION PROVIDER
    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(
                        customUserDetailsService
                );


        provider.setPasswordEncoder(
                passwordEncoder()
        );


        return provider;
    }

    // AUTHENTICATION MANAGER
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration
                .getAuthenticationManager();
    }
}