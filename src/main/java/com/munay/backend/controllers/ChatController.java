package com.munay.backend.controllers;

import com.munay.backend.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.security.Principal;

@Controller
@CrossOrigin(origins = "http://localhost:3000") // Permite peticiones desde React

public class ChatController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("/private-message")
    public Message receivePrivateMessage(@Payload Message message, Principal principal) {
        System.out.println("Mensaje enviado por: " + principal.getName());
        System.out.println("Mensaje para: " + message.getReceiverName());

        simpMessagingTemplate.convertAndSendToUser(
                message.getReceiverName(), "/private", message
        );
        return message;
    }
}
