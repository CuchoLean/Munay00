package com.munay.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HolaController {

    @GetMapping("/hola")
    public String saludar() {
        return "¡Hola Mundo desde Spring Boot!";
    }
    //mongod --dbpath "D:\data"
}
