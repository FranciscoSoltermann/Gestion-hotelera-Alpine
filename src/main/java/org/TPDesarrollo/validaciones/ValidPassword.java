package org.TPDesarrollo.validaciones;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Anotación personalizada para validar contraseñas.
 * Utiliza el validador PasswordConstraintValidator para implementar la lógica de validación.
 * Puede aplicarse a campos y parámetros.
 * Incluye un mensaje de error por defecto que puede ser sobrescrito.
 * Se retiene en tiempo de ejecución para que el validador pueda acceder a ella.
 */
@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class) // Apunta al validador
@Target({ ElementType.FIELD, ElementType.PARAMETER }) // Se puede usar en campos
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "La contraseña es inválida"; // Mensaje por defecto
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}