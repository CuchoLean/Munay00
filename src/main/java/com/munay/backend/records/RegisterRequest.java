package com.munay.backend.records;

import jakarta.validation.constraints.*;

import java.util.List;

public record RegisterRequest(

        @NotBlank(message = "El nombre es obligatorio.")
        String name,

        @NotBlank(message = "El email es obligatorio.")
        @Email(message = "El email debe ser válido.")
        String email,

        @NotBlank(message = "La contraseña es obligatoria.")
        String password,
        @NotBlank(message = "Debes repetir la contraseña.")
        String confirmPassword,
        @NotNull(message = "La edad es obligatoria.")
        @Min(value = 18, message = "La edad debe ser al menos 18 años.")
        int age,

        @NotNull(message = "El teléfono es obligatorio.")
        @Min(value = 600000000, message = "El teléfono debe ser un número válido.")
        @Max(value = 799999999, message = "El teléfono debe ser un número válido.")
        long tel,

        @NotBlank(message = "La biografía es obligatoria.")
        String bio,
        @NotNull(message = "La primera foto es obligatoria")
        byte[] foto1,
        @NotNull(message = "La segunda foto es obligatoria")
        byte[] foto2
        //List<String> likes,
        //List<String> matches
) {
}
