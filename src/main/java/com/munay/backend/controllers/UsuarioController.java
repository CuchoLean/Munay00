package com.munay.backend.controllers;

import com.munay.backend.enums.Genero;
import com.munay.backend.models.Match;
import com.munay.backend.models.Usuario;
import com.munay.backend.repositories.MessageRepository;
import com.munay.backend.repositories.PostRepository;
import com.munay.backend.services.JwtService;
import com.munay.backend.repositories.MatchRepository;
import com.munay.backend.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
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
    private MessageRepository messageRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;


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


    @PostMapping("/like/{likedUserId}")
    public ResponseEntity<String> likeUser(Authentication authentication, @PathVariable String likedUserId) {
        String email = authentication.getName(); // email del JWT
        Usuario user = usuarioRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario autenticado no encontrado");
        }

        Usuario likedUser = usuarioRepository.findById(likedUserId).orElse(null);
        if (likedUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario al que se quiere dar like no encontrado");
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
        if (likedUser.getLikes() != null && likedUser.getLikes().contains(user.getId())) {
            boolean alreadyMatched = matchRepository.existsByIdUsuario1AndIdUsuario2(user.getId(), likedUserId)
                    || matchRepository.existsByIdUsuario2AndIdUsuario1(user.getId(), likedUserId);

            if (!alreadyMatched) {
                Match match = Match.builder()
                        .idUsuario1(user.getId())
                        .idUsuario2(likedUserId)
                        .build();
                matchRepository.save(match);
            }

            return ResponseEntity.ok("¡Has hecho Match, felicidades!");
        }

        return ResponseEntity.ok("El like ha sido enviado.");
    }

    @GetMapping("/usuarios-match")
    public ResponseEntity<List<Usuario>> obtenerUsuariosConMatch(Authentication authentication) {
        String email = authentication.getName(); // email del usuario desde JWT
        Usuario actual = usuarioRepository.findByEmail(email);

        if (actual == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String userId = actual.getId();

        // Filtrar los matches donde participa el usuario autenticado sin usar stream
        List<Match> matches = new ArrayList<>();
        for (Match m : matchRepository.findAll()) {
            if (Objects.equals(m.getIdUsuario1(), userId) || Objects.equals(m.getIdUsuario2(), userId)) {
                matches.add(m);
            }
        }

        List<String> otrosIds = new ArrayList<>();
        for (Match m : matches) {
            String otroId = Objects.equals(m.getIdUsuario1(), userId) ? m.getIdUsuario2() : m.getIdUsuario1();
            if (otroId != null && !otrosIds.contains(otroId)) {
                otrosIds.add(otroId);
            }
        }

        List<Usuario> usuarios = usuarioRepository.findAllById(otrosIds);

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/todosL")
    public ResponseEntity<List<Usuario>> getUsuariosNoLikeados(Authentication authentication) {
        String email = authentication.getName(); // sub del JWT
        Usuario actual = usuarioRepository.findByEmail(email);

        if (actual == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<String> likes = actual.getLikes() != null ? actual.getLikes() : List.of();

        List<Usuario> noLikeados = new ArrayList<>();
        for (Usuario u : usuarioRepository.findAll()) {
            if (!likes.contains(u.getId()) && !u.getId().equals(actual.getId())) {
                noLikeados.add(u);
            }
        }

        // Mezclar la lista aleatoriamente
        Collections.shuffle(noLikeados);

        return ResponseEntity.ok(noLikeados);
    }

    @GetMapping("/buscarUsuarioToken")
    public ResponseEntity<Usuario> obtenerUsuarioPorToken(Authentication authentication) {
        String email = authentication.getName(); // extraído del 'sub' del JWT
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(usuario);
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
        usuarioExistente.setPassword(passwordEncoder.encode(datosNuevos.getPassword()));
        usuarioExistente.setAge(datosNuevos.getAge());
        usuarioExistente.setTel(datosNuevos.getTel());
        usuarioExistente.setBio(datosNuevos.getBio());
        usuarioExistente.setFoto1(datosNuevos.getFoto1());
        usuarioExistente.setFoto2(datosNuevos.getFoto2());

        Usuario actualizado = usuarioRepository.save(usuarioExistente);
        return new ResponseEntity<>(actualizado, HttpStatus.OK);
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<String> eliminarUsuarioPorToken(Authentication authentication) {
        String email = authentication.getName();  // Email extraído del JWT
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }

        String userId = usuario.getId();

        messageRepository.deleteBySenderNameOrReceiverName(userId, userId);
        postRepository.deleteByIdUsuario(userId);
        matchRepository.deleteByIdUsuario1OrIdUsuario2(userId, userId);

        usuarioRepository.delete(usuario);

        return new ResponseEntity<>("Usuario eliminado correctamente", HttpStatus.OK);
    }

    @GetMapping("/usuarios-sin-admin")
    public ResponseEntity<List<Usuario>> obtenerUsuariosSinAdmin() {
        List<Usuario> usuarios = usuarioRepository.findByGeneroNot(Genero.ADMIN);
        return ResponseEntity.ok(usuarios);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarUsuarioPorId(@PathVariable String id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);

        if (usuario == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }

        // Eliminar mensajes, posts y matches relacionados
        messageRepository.deleteBySenderNameOrReceiverName(id, id);
        postRepository.deleteByIdUsuario(id);
        matchRepository.deleteByIdUsuario1OrIdUsuario2(id, id);

        usuarioRepository.delete(usuario);

        return new ResponseEntity<>("Usuario eliminado correctamente", HttpStatus.OK);
    }
}