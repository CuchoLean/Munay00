package com.munay.backend.repositories;

import com.munay.backend.enums.Genero;
import com.munay.backend.models.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Usuario findByEmail(String email);
    boolean existsByEmail(String email);
    List<Usuario> findByGeneroNot(Genero genero);

}
