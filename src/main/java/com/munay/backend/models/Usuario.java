package com.munay.backend.models;

import com.munay.backend.enums.Genero;
import com.munay.backend.validators.UniqueEmail;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private String email;
    private String password;
    @Min(value = 18, message = "La edad debe ser al menos 18 años.")
    private int age;

    @NotBlank(message = "La biografía es obligatoria.")
    private String bio;

    @NotNull(message = "La foto 1 es obligatoria.")
    private byte[] foto1;

    @NotNull(message = "La foto 2 es obligatoria.")
    private byte[] foto2;

    @NotNull(message = "El género es obligatorio.")
    private Genero genero;

    private boolean fumador;

    private List<String> likes;

}
