package com.munay.backend.controllers;

import com.munay.backend.models.Match;
import com.munay.backend.models.Usuario;
import com.munay.backend.repositories.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matches")
public class MatchController {
    @Autowired
    private MatchRepository matchRepository;

    @PostMapping("/crear")
    public Match crear(@RequestBody Match match) {
        return matchRepository.save(match);
    }

    // Obtener todos los usuarios
    @GetMapping("/todos")
    public List<Match> obtenerTodos() {
        return matchRepository.findAll();
    }

}
