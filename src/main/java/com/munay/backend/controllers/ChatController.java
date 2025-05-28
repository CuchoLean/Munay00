package com.munay.backend.controllers;

import com.munay.backend.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/private-message")
    public void recMessage(@Payload Message message) {
        simpMessagingTemplate.convertAndSend("/user/" + message.getReceiverName() + "/private", message);
    }
}