package org.TPDesarrollo.Validaciones;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;


public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isEmpty()) {
            return false; // Puedes usar @NotBlank para esto, pero es bueno tenerlo
        }

        // Extraemos letras y números
        List<Character> letras = password.chars()
                .filter(Character::isLetter)
                .mapToObj(c -> (char) c)
                .toList();

        List<Integer> numeros = password.chars()
                .filter(Character::isDigit)
                .map(Character::getNumericValue) // Convierte '1' a 1
                .boxed()
                .toList();

        // --- 1. Revisar longitudes ---
        if (letras.size() < 5) {
            setErrorMessage(context, "La contraseña debe tener al menos 5 letras.");
            return false;
        }
        if (numeros.size() < 3) {
            setErrorMessage(context, "La contraseña debe tener al menos 3 números.");
            return false;
        }

        // --- 2. Revisar números idénticos (ej. "777") ---
        if (numeros.stream().distinct().count() == 1) {
            setErrorMessage(context, "Los números no pueden ser todos iguales (ej. '333').");
            return false;
        }

        // --- 3. Revisar números consecutivos (ej. "123" o "321") ---
        for (int i = 0; i < numeros.size() - 2; i++) {
            int n1 = numeros.get(i);
            int n2 = numeros.get(i + 1);
            int n3 = numeros.get(i + 2);

            if (n1 + 1 == n2 && n2 + 1 == n3) {
                setErrorMessage(context, "Los números no pueden ser consecutivos (ej. '123').");
                return false;
            }
            if (n1 - 1 == n2 && n2 - 1 == n3) {
                setErrorMessage(context, "Los números no pueden ser consecutivos (ej. '321').");
                return false;
            }
        }

        return true; // ¡Válida!
    }

    // Método privado para establecer el mensaje de error específico
    private void setErrorMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}