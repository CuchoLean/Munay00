package com.munay.backend.repositories;

import com.munay.backend.models.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    // Ejemplo de búsqueda personalizada
    Usuario findByEmail(String email);
}
