package com.munay.backend.repositories;

import com.munay.backend.controllers.MatchController;
import com.munay.backend.models.Match;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchRepository  extends MongoRepository<Match,String> {
        Match findByEmailUsuario1AndEmailUsuario2(String email1, String email2);
}
