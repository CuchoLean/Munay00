package com.munay.backend.validators;

import com.munay.backend.records.RegisterRequest;
import com.munay.backend.repositories.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterRequest> {

    @Override
    public boolean isValid(RegisterRequest request, ConstraintValidatorContext context) {
        // Si password o confirmPassword son nulos o están vacíos, NO validamos la coincidencia aquí,
        // porque las otras validaciones (@NotBlank) deberían encargarse de eso.
        if (request.password() == null || request.password().isBlank() ||
                request.confirmPassword() == null || request.confirmPassword().isBlank()) {
            return true;  // Dejar que el @NotBlank lance la violación.
        }

        boolean isValid = request.password().equals(request.confirmPassword());

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Las contraseñas no coinciden.")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
