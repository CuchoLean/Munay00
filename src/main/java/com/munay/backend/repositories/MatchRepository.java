package com.munay.backend.repositories;

import com.munay.backend.controllers.MatchController;
import com.munay.backend.models.Match;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchRepository  extends MongoRepository<Match,String> {
        boolean existsByIdUsuario1AndIdUsuario2(String idUsuario1, String idUsuario2);
        boolean existsByIdUsuario2AndIdUsuario1(String idUsuario2, String idUsuario1); // Corrected
        void deleteByIdUsuario1OrIdUsuario2(String idUsuario1, String idUsuario2);

}
