package com.munay.backend.repositories;

import com.munay.backend.models.Usuario;
import com.munay.backend.records.LoginRequest;
import com.munay.backend.records.RegisterRequest;
import com.munay.backend.records.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenResponse register(RegisterRequest request) {

        //recibo string en b64 y los convierto en bytes
        String foto1Base64 = request.foto1().split(",")[1];
        String foto2Base64 = request.foto2().split(",")[1];
        byte[] foto1 = Base64.getDecoder().decode(foto1Base64);
        byte[] foto2 = Base64.getDecoder().decode(foto2Base64);

        Usuario usuario = Usuario.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .age(request.age())
                .tel(request.tel())
                .bio(request.bio())
                .foto1(foto1)
                .foto2(foto2)
                .build();
        usuarioRepository.save(usuario);
        var jwtToken = jwtService.generateToken(usuario);
        var refreshToken = jwtService.generateRefreshToken(usuario);
        return new TokenResponse(jwtToken, refreshToken);
    }

    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email()
                        , request.password()
                ));
        var usuario = usuarioRepository.findByEmail(request.email());
        var jwtToken = jwtService.generateToken(usuario);
        var refreshToken = jwtService.generateRefreshToken(usuario);
        return new TokenResponse(jwtToken, refreshToken);
    }

    public TokenResponse refreshToken(final String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Bearer token");
        }
        final String refreshToken = authHeader.substring(7);
        final String email = jwtService.extractUsername(refreshToken);
        if (email == null) {
            throw new IllegalArgumentException("Invalid Refresh Token");
        }
        final Usuario usuario = usuarioRepository.findByEmail(email);
        if (!jwtService.isTokenValid(refreshToken, usuario)) {
            throw new IllegalArgumentException("Invalid Refresh Token");
        }
        final String accesToken = jwtService.generateToken(usuario);
        return new TokenResponse(accesToken, refreshToken);
    }

}
