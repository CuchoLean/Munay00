package com.munay.backend.records;

import com.munay.backend.enums.Genero;
import com.munay.backend.repositories.PasswordMatches;
import jakarta.validation.constraints.*;
import com.munay.backend.validators.UniqueEmail;

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
        @Min(value = 18, message = "La edad debe ser al menos 18 años.")
        int age,
        @NotBlank(message = "La descripción es obligatoria.")
        String bio,
        @NotBlank(message = "La foto 1 es obligatoria.")
        String foto1,
        @NotBlank(message = "La foto 2 es obligatoria.")
        String foto2,
        @NotNull(message = "El género es obligatorio.")
        Genero genero,
        boolean fumador
) {
}
