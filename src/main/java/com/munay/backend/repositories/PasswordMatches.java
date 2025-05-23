package com.munay.backend.repositories;


import com.munay.backend.config.PasswordMatchesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatches {
    String message() default "Las contraseñas no coinciden.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
