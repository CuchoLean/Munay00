package com.munay.backend.records;

import com.munay.backend.repositories.PasswordMatches;
import jakarta.validation.constraints.*;
import com.munay.backend.repositories.UniqueEmail;

import java.util.List;

@PasswordMatches
public record RegisterRequest(

        @NotBlank(message = "El nombre es obligatorio.")
        String name,

        @NotBlank(message = "El email es obligatorio.")
        @Email(message = "El email debe ser válido.")
        @UniqueEmail(message = "El email ya está registrado.")
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
        @NotBlank(message = "La foto 1 es obligatoria.")
        String foto1,
        @NotBlank(message = "La foto 2 es obligatoria.")
        String foto2
) {
}
