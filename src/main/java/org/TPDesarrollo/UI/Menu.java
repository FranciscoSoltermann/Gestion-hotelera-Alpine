package org.TPDesarrollo.UI;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import org.TPDesarrollo.Excepciones.ContraseniaInvalida;
import org.TPDesarrollo.Excepciones.UsuarioNoEncontrado;
import org.TPDesarrollo.Gestores.GestorHuesped;
import org.TPDesarrollo.Gestores.GestorUsuario;
import org.TPDesarrollo.UI.acciones.BuscarHuespedUI;

import java.io.Console;
import java.util.Scanner;

@Component
public class Menu implements CommandLineRunner {

    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    private GestorHuesped gestorHuesped;

    @Autowired
    private GestorUsuario gestorUsuario;

    @Override
    public void run(String... args) throws Exception {
        iniciar();
    }

    public void iniciar() {
        if (autenticar()) {
            System.out.println("\nCargando menú principal del sistema...");
            mostrarMenuPrincipal();
        }
        scanner.close();
    }

    private void mostrarMenuPrincipal() {
        int opcion;
        do {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Buscar Huésped (CU02)");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        AccionMenu buscarHuespedAccion = new BuscarHuespedUI(scanner, gestorHuesped);
                        buscarHuespedAccion.ejecutar();
                        break;
                    case 0:
                        System.out.println("Saliendo del sistema. ¡Hasta luego!");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                opcion = -1; // Para continuar el bucle
            }
        } while (opcion != 0);
    }

    private boolean autenticar() {
        System.out.println("=== BIENVENIDO AL SISTEMA DE GESTIÓN HOTELERA ===");
        Console console = System.console();
        while (true) {
            try {
                System.out.print("Usuario: ");
                String nombre = scanner.nextLine();
                String contrasenia;

                if (console != null) {
                    contrasenia = new String(console.readPassword("Contraseña: "));
                } else {
                    System.out.print("Contraseña (visible): ");
                    contrasenia = scanner.nextLine();
                }

                if (gestorUsuario.autenticarUsuario(nombre, contrasenia)) {
                    System.out.println("\n✅ ¡Autenticación exitosa! Bienvenido/a, " + nombre + ".");
                    return true;
                }
            } catch (UsuarioNoEncontrado | ContraseniaInvalida e) {
                System.err.println("\n❌ Error: " + e.getMessage() + " Intente de nuevo.\n");
            }
        }
    }

    
}
