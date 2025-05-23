package com.munay.backend.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "usuarios")
public class Usuario {

    @Id
    private String id;
    @NotBlank(message = "El nombre es obligatorio.")
    private String name;

    @NotBlank(message = "El email es obligatorio.")
    @Email(message = "El email debe ser válido.")
    @Indexed(unique = true)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria.")
    private String password;

    @Min(value = 18, message = "La edad debe ser al menos 18 años.")
    private int age;

    @NotNull(message = "El teléfono es obligatorio.")
    @Min(value = 600000000, message = "El teléfono debe ser un número válido.")
    @Max(value = 799999999, message = "El teléfono debe ser un número válido.")
    private long tel;

    @NotBlank(message = "La biografía es obligatoria.")
    private String bio;
    @NotNull(message = "La primera foto es obligatoria")
    byte[] foto1;
    @NotNull(message = "La segunda foto es obligatoria")
    byte[] foto2;

   // private List<String> fotosUrl;
    //private List<String> likes;    // IDs de usuarios a los que le dio like
    //private List<String> matches;  // IDs de matches confirmados
}

