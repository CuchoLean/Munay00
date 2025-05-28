package com.munay.backend.controllers;

import com.munay.backend.models.Match;
import com.munay.backend.models.Message;
import com.munay.backend.models.Usuario;
import com.munay.backend.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mensajes")
@CrossOrigin(origins = "http://localhost:3000") // Permite peticiones desde React
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;


    @PostMapping("/crear")
    public Message crearMensaje(@RequestBody Message message) {
        message.setDate(String.valueOf(LocalDateTime.now()));
        return messageRepository.save(message);
    }
    @GetMapping("/historial")
    public List<Message> obtenerHistorial(@RequestParam String user1, @RequestParam String user2) {
        return messageRepository.findBySenderNameAndReceiverName(user1, user2);
    }
}
