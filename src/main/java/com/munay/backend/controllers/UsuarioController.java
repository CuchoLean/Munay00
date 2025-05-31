package com.munay.backend.controllers;

import com.munay.backend.models.Match;
import com.munay.backend.models.Usuario;
import com.munay.backend.services.JwtService;
import com.munay.backend.repositories.MatchRepository;
import com.munay.backend.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:3000") // Permite peticiones desde React
public class UsuarioController {
    // Crear usuario
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private JwtService jwtService;


    @PostMapping("/crear")
    public Usuario crearUsuario(@Valid @RequestBody Usuario usuario) {
        System.out.println("intento de creacion de usuario");
        return usuarioRepository.save(usuario);
    }

    // Obtener todos los usuarios
    @GetMapping("/todos")
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    // Buscar usuario por email
    @GetMapping("/buscar")
    public ResponseEntity<Usuario> buscarPorEmail(@RequestParam String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }


    @PostMapping("/{userId}/like/{likedUserId}")
    public ResponseEntity<String> likeUser(@PathVariable String userId, @PathVariable String likedUserId) {
        Usuario user = usuarioRepository.findById(userId).orElse(null);
        Usuario likedUser = usuarioRepository.findById(likedUserId).orElse(null);

        if (user == null || likedUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        // Inicializar likes si es null
        if (user.getLikes() == null) {
            user.setLikes(new java.util.ArrayList<>());
        }

        // Agregar el like si aún no lo tiene
        if (!user.getLikes().contains(likedUserId)) {
            user.getLikes().add(likedUserId);
            usuarioRepository.save(user);
        }

        // Verificar si likedUser también dio like a user
        if (likedUser.getLikes() != null && likedUser.getLikes().contains(userId)) {
            boolean alreadyMatched = matchRepository.existsByIdUsuario1AndIdUsuario2(userId, likedUserId)
                    || matchRepository.existsByIdUsuario2AndIdUsuario1(userId, likedUserId);

            if (!alreadyMatched) {
                Match match = Match.builder()
                        .idUsuario1(userId)
                        .idUsuario2(likedUserId)
                        .build();
                matchRepository.save(match);
            }

            return ResponseEntity.ok("¡Has hecho Match, felicidades!");
        }

        return ResponseEntity.ok("El like ha sido enviado.");
    }

    @GetMapping("/{userId}/usuarios-match")
    public ResponseEntity<List<Usuario>> obtenerUsuariosConMatch(@PathVariable String userId) {
        // Paso 1: Filtrar los matches en los que participa
        List<Match> matches = matchRepository.findAll().stream()
                .filter(m -> Objects.equals(m.getIdUsuario1(), userId) || Objects.equals(m.getIdUsuario2(), userId))
                .toList();

        List<String> otrosIds = matches.stream()
                .map(m -> Objects.equals(m.getIdUsuario1(), userId) ? m.getIdUsuario2() : m.getIdUsuario1())
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // Paso 3: Obtener los usuarios de la base de datos
        List<Usuario> usuarios = usuarioRepository.findAllById(otrosIds);

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/todosL")
    public List<Usuario> getUsuariosNoLikeados(@RequestHeader("Authorization") String token) {
        String email = jwtService.extractUsername(token.substring(7)); // Quitar "Bearer "
        Usuario actual = usuarioRepository.findByEmail(email);

        List<String> likes = actual.getLikes() != null ? actual.getLikes() : List.of();

        return usuarioRepository.findAll().stream()
                .filter(u -> !likes.contains(u.getId()) && !u.getId().equals(actual.getId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/buscarUsuarioToken")
    public ResponseEntity<Usuario> obtenerUsuarioPorToken(@RequestHeader("Authorization") String token) {
        String email = jwtService.extractUsername(token.substring(7)); // quita "Bearer "
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @PutMapping("/actualizar")
    public ResponseEntity<Usuario> actualizarUsuario(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody Usuario datosNuevos) {

        String email = jwtService.extractUsername(token.substring(7)); // quitar "Bearer "
        Usuario usuarioExistente = usuarioRepository.findByEmail(email);

        if (usuarioExistente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Solo actualizar campos válidos
        usuarioExistente.setName(datosNuevos.getName());
        usuarioExistente.setPassword(datosNuevos.getPassword());
        usuarioExistente.setAge(datosNuevos.getAge());
        usuarioExistente.setTel(datosNuevos.getTel());
        usuarioExistente.setBio(datosNuevos.getBio());
        usuarioExistente.setFoto1(datosNuevos.getFoto1());
        usuarioExistente.setFoto2(datosNuevos.getFoto2());

        Usuario actualizado = usuarioRepository.save(usuarioExistente);
        return new ResponseEntity<>(actualizado, HttpStatus.OK);
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<String> eliminarUsuarioPorToken(@RequestHeader("Authorization") String token) {
        String email = jwtService.extractUsername(token.substring(7)); // Quita "Bearer "
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }

        usuarioRepository.delete(usuario);
        return new ResponseEntity<>("Usuario eliminado correctamente", HttpStatus.OK);
    }

}
