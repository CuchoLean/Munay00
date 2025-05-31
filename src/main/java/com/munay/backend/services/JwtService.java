package com.munay.backend.services;

import com.munay.backend.models.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public String generateToken(final Usuario usuario){
        return buildToken(usuario,jwtExpiration);
    }
    public  String generateRefreshToken(final Usuario usuario){
        return buildToken(usuario,refreshExpiration);
    }

    private String buildToken(final Usuario usuario, final long expiration) {
    return Jwts.builder()
            .claims(Map.of("id", usuario.getId()))
            .subject(usuario.getEmail()).issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey())
            .compact();
    }
    private SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String refreshToken) {
        try {
            final Claims jwtToken = Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload();
            return jwtToken.getSubject();
        } catch (io.jsonwebtoken.security.SignatureException e) {
            System.out.println("Token inválido: la firma no coincide.");
            return null;
        } catch (Exception e) {
            System.out.println("Error al procesar el token: " + e.getClass().getSimpleName());
            return null;
        }
    }

    public boolean isTokenValid(String refreshToken, Usuario usuario) {
        final String email = extractUsername(refreshToken);
        return (email.equals(usuario.getEmail()) && !isTokenExpired(refreshToken));
    }

    private boolean isTokenExpired(String refreshToken) {
        return extractExpiration(refreshToken).before(new Date());
    }

    private Date extractExpiration(String refreshToken) {
        final Claims jwtToken = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload();
        return jwtToken.getExpiration();
    }
}
