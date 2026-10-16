package edu.unam.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration}")
    private long expiration;


    public String generarToken(UserDetails userDetails) {

        List<String> roles = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + expiration
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }


    public String obtenerUsername(String token) {

        return obtenerClaim(
                token,
                Claims::getSubject
        );
    }


    public Date obtenerExpiracion(String token) {

        return obtenerClaim(
                token,
                Claims::getExpiration
        );
    }


    public <T> T obtenerClaim(
            String token,
            Function<Claims, T> resolver) {

        Claims claims =
                obtenerTodosLosClaims(token);

        return resolver.apply(claims);
    }


    public boolean esTokenValido(
            String token,
            UserDetails userDetails) {

        String username =
                obtenerUsername(token);

        return username.equals(
                userDetails.getUsername()
        ) && !estaExpirado(token);
    }


    private boolean estaExpirado(String token) {

        return obtenerExpiracion(token)
                .before(new Date());
    }


    private Claims obtenerTodosLosClaims(
            String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    private SecretKey getSigningKey() {

        byte[] keyBytes =
                Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}