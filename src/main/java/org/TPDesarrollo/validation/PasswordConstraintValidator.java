package org.TPDesarrollo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * Validador personalizado para la anotación @ValidPassword.
 * Implementa las reglas de validación para contraseñas:
 * 1. Al menos 5 letras y 3 números.
 * 2. No más de 2 números idénticos consecutivos.
 * 3. No más de 2 números consecutivos en secuencia ascendente o descendente.
 */
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    /** Valida la contraseña según las reglas definidas.
     *
     * @param password La contraseña a validar.
     * @param context  El contexto de validación.
     * @return true si la contraseña es válida, false en caso contrario.
     */
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isEmpty()) {
            return false; // Puedes usar @NotBlank para esto, pero es bueno tenerlo
        }

        // Extraemos letras y números
        List<Character> letras = password.chars()
                .filter(Character::isLetter)
                .mapToObj(c -> (char) c)
                .toList();

        // Convertimos caracteres numéricos a enteros
        List<Integer> numeros = password.chars()
                .filter(Character::isDigit)
                .map(Character::getNumericValue) // Convierte '1' a 1
                .boxed()
                .toList();

        //Revisar longitudes
        if (letras.size() < 5) {
            setErrorMessage(context, "La contraseña debe tener al menos 5 letras.");
            return false;
        }
        if (numeros.size() < 3) {
            setErrorMessage(context, "La contraseña debe tener al menos 3 números.");
            return false;
        }

        //Revisar números idénticos
        if (numeros.stream().distinct().count() == 1) {
            setErrorMessage(context, "Los números no pueden ser todos iguales (ej. '333').");
            return false;
        }

        //Revisar números consecutivos
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

        return true;
    }

    /** Establece un mensaje de error personalizado en el contexto de validación.
     *
     * @param context El contexto de validación.
     * @param message El mensaje de error a establecer.
     */
    private void setErrorMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}