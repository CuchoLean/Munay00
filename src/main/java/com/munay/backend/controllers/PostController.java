package com.munay.backend.controllers;

import com.munay.backend.models.Post;
import com.munay.backend.models.Usuario;
import com.munay.backend.repositories.PostRepository;
import com.munay.backend.repositories.UsuarioRepository;
import com.munay.backend.services.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Crear un nuevo post
    @PostMapping
    public ResponseEntity<Post> crearPost(@RequestBody @Valid Post post, Authentication authentication) {
        // Obtienes el nombre del usuario logueado
        String email = authentication.getName(); // `sub` del JWT = email

        // Buscas al usuario en la base de datos
        Usuario usuario = usuarioRepository.findByEmail(email);

        // Asignas el usuario al post antes de guardar
        post.setIdUsuario(usuario.getId());
        post.setNombreUsuario(usuario.getName());

        Post nuevoPost = postRepository.save(post);
        return ResponseEntity.ok(nuevoPost);
    }


    // Ver todos los posts
    @GetMapping
    public ResponseEntity<List<Post>> obtenerTodos() {
        return ResponseEntity.ok(postRepository.findAll());
    }


    @GetMapping("/usuario")
    public ResponseEntity<List<Post>> obtenerPorUsuario(Authentication authentication) {
        String email = authentication.getName(); // `sub` del JWT = email
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String idUsuario = usuario.getId(); // Obtener el ID real desde la BD
        List<Post> posts = postRepository.findByIdUsuario(idUsuario);
        return ResponseEntity.ok(posts);
    }


    // Editar un post existente
    @PutMapping("/{id}")
    public ResponseEntity<Post> editarPost(@PathVariable String id, @RequestBody @Valid Post postActualizado) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            post.setTexto(postActualizado.getTexto());
            post.setImagen(postActualizado.getImagen());
            return ResponseEntity.ok(postRepository.save(post));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un post
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPost(@PathVariable String id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> obtenerPostPorId(@PathVariable String id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            return ResponseEntity.ok(optionalPost.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
