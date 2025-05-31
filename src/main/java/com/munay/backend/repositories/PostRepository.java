package com.munay.backend.repositories;

import com.munay.backend.models.Match;
import com.munay.backend.models.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post,String> {
}
