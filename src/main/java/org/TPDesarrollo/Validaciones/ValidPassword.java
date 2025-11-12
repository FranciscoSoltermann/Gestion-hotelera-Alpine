package org.TPDesarrollo.Validaciones;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class) // Apunta al validador
@Target({ ElementType.FIELD, ElementType.PARAMETER }) // Se puede usar en campos
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "La contraseña es inválida"; // Mensaje por defecto
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}