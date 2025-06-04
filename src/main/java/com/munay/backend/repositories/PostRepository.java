package com.munay.backend.repositories;

import com.munay.backend.models.Match;
import com.munay.backend.models.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post,String> {
    void deleteByIdUsuario(String idUsuario);
    List<Post> findByIdUsuario(String idUsuario);
}
