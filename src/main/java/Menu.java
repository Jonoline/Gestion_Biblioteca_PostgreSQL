import java.util.Scanner;
import java.sql.Connection;

public class Menu {
    private static final Scanner sc = new Scanner(System.in);
    private static GestorBiblioteca gestor;

    public static void menu(Connection con) {
        gestor = new GestorBiblioteca(con);
        int opcion;
        do {
            mostrarOpciones();
            opcion = leerEntero("Opcion: ");
            if (opcion != 6) {
                ejecutarOpcion(opcion);
            }
        } while (opcion != 6);
    }

    private static int leerEntero(String mensaje) {
        int numero = -1;
        boolean leido = false;
        while (!leido) {
            System.out.print(mensaje);
            try {
                String input = sc.nextLine().trim();
                numero = Integer.parseInt(input);
                leido = true;
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número entero válido. ");
            }
        }
        return numero;
    }

    public static void mostrarOpciones() {
        System.out.println("\n=============================");
        System.out.println("      Gestión Biblioteca Universitaria     ");
        System.out.println("============================= ");
        System.out.println("    [1] Crear/Añadir un Libro ");
        System.out.println("    [2] Mostrar Libros ");
        System.out.println("    [3] Actualizar la informacion de un Libro ");
        System.out.println("    [4] Borrar un Libro ");
        System.out.println("    [5] Realizar Préstamo de un Libro ");
        System.out.println("    [6] Salir ");
        System.out.println("============================= ");
    }

    public static void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> {
                System.out.print("Ingrese el título del libro: ");
                String titulo = sc.nextLine();

                System.out.print("Ingrese el nombre del Autor: ");
                String autor = sc.nextLine();

                System.out.print("Ingrese el nombre de la editorial: ");
                String editorial = sc.nextLine();

                gestor.crearLibro(titulo, autor, editorial);
            }
            case 2 -> gestor.mostrarLibros();

            case 3 -> {
                System.out.println("\n--- ACTUALIZAR LIBRO ---");
                int id = leerEntero("Ingrese el ID del libro a editar: ");

                System.out.print("Ingrese el nuevo Título: ");
                String nuevoTitulo = sc.nextLine();

                System.out.print("Ingrese el nuevo autor: ");
                String nuevoAutor = sc.nextLine();

                System.out.print("Ingrese el nombre de la nueva editorial: ");
                String nuevaEditorial = sc.nextLine();

                gestor.actualizarLibro(id, nuevoTitulo, nuevoAutor, nuevaEditorial);
            }
            case 4 -> {
                int id = leerEntero("Ingrese el ID del libro a borrar: ");
                gestor.borrarLibro(id);
            }
            case 5 -> {
                int idLibro = leerEntero("Ingrese el ID del libro a prestar: ");
                int idCliente = leerEntero("Ingrese el ID del cliente que pide el prestamo: ");

                gestor.realizarPrestamo(idLibro, idCliente);
            }
            case 6 -> System.out.println("Saliendo...");

            default -> System.out.println("Opción no válida, intente entre 1 y 6.");
        }
    }
}