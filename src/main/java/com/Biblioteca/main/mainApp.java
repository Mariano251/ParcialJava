package com.Biblioteca.main;

import com.Biblioteca.DAO.AutorDAO;
import com.Biblioteca.DAO.LibroDAO;
import com.Biblioteca.DAO.impl.AutorDAOImpl;
import com.Biblioteca.DAO.impl.LibroDAOImpl;
import com.Biblioteca.model.Autor;
import com.Biblioteca.model.Libro;
import com.Biblioteca.util.DatabaseUtil;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class mainApp {
    private static final Logger logger = LoggerFactory.getLogger(mainApp.class);
    private static final Scanner scanner = new Scanner(System.in);
    private static final AutorDAO autorDAO = new AutorDAOImpl();
    private static final LibroDAO libroDAO = new LibroDAOImpl();

    public static void main(String[] args) {
        DatabaseUtil.initializeDatabase(); // Asegura que las tablas existan

        System.out.println("¡Bienvenido al sistema de gestion de Biblioteca!");
        int option;
        do {
            displayMenu();
            option = getUserOption();

            try {
                switch (option) {
                    case 1:
                        createAutor();
                        break;
                    case 2:
                        listAutores();
                        break;
                    case 3:
                        findAutorById();
                        break;
                    case 4:
                        updateAutor();
                        break;
                    case 5:
                        deleteAutor();
                        break;
                    case 6:
                        createLibro();
                        break;
                    case 7:
                        listLibros();
                        break;
                    case 8:
                        findLibroById();
                        break;
                    case 9:
                        updateLibro();
                        break;
                    case 10:
                        deleteLibro();
                        break;
                    case 0:
                        System.out.println("Saliendo del sistema. ¡Hasta luego!");
                        break;
                    default:
                        System.out.println("Opcion no valida. Por favor, intente de nuevo.");
                }
            } catch (SQLException e) {
                logger.error("Error en operacion de base de datos: {}", e.getMessage(), e);
                System.err.println("Ocurrio un error en la base de datos: " + e.getMessage());
            } catch (Exception e) {
                logger.error("Error inesperado: {}", e.getMessage(), e);
                System.err.println("Ocurrio un error inesperado: " + e.getMessage());
            }
            System.out.println("\nPresione Enter para continuar...");
            scanner.nextLine();
        } while (option != 0);

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("Gestion de Autores:");
        System.out.println("  1. Crear Autor");
        System.out.println("  2. Listar Autores");
        System.out.println("  3. Buscar Autor por ID");
        System.out.println("  4. Actualizar Autor");
        System.out.println("  5. Eliminar Autor");
        System.out.println("Gestion de Libros:");
        System.out.println("  6. Crear Libro");
        System.out.println("  7. Listar Libros");
        System.out.println("  8. Buscar Libro por ID");
        System.out.println("  9. Actualizar Libro");
        System.out.println("  10. Eliminar Libro");
        System.out.println("  0. Salir");
        System.out.print("Seleccione una opcion: ");
    }

    private static int getUserOption() {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada invalida. Por favor, ingrese un numero.");
            scanner.next(); // Consume la entrada invalida
            System.out.print("Seleccione una opcion: ");
        }
        int option = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea
        return option;
    }

    // Metodos CRUD para Autor
    private static void createAutor() throws SQLException {
        System.out.println("\n--- Crear Nuevo Autor ---");
        System.out.print("Nombre del Autor: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido del Autor: ");
        String apellido = scanner.nextLine();

        // Validacion basica de entrada
        if (nombre.trim().isEmpty() || apellido.trim().isEmpty()) {
            System.out.println("Error: Nombre y apellido no pueden estar vacios.");
            return;
        }

        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setApellido(apellido);
        autorDAO.save(autor);
        System.out.println("Autor creado exitosamente con ID: " + autor.getId());
    }

    private static void listAutores() throws SQLException {
        System.out.println("\n--- Lista de Autores ---");
        List<Autor> autores = autorDAO.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            autores.forEach(System.out::println);
        }
    }

    private static void findAutorById() throws SQLException {
        System.out.println("\n--- Buscar Autor por ID ---");
        System.out.print("Ingrese el ID del Autor a buscar: ");
        if (!scanner.hasNextLong()) {
            System.out.println("ID invalido. Por favor, ingrese un numero.");
            scanner.next();
            return;
        }
        Long id = scanner.nextLong();
        scanner.nextLine();

        Optional<Autor> autor = autorDAO.findById(id);
        if (autor.isPresent()) {
            System.out.println("Autor encontrado: " + autor.get());
        } else {
            System.out.println("No se encontro ningun autor con ID: " + id);
        }
    }

    private static void updateAutor() throws SQLException {
        System.out.println("\n--- Actualizar Autor ---");
        System.out.print("Ingrese el ID del Autor a actualizar: ");
        if (!scanner.hasNextLong()) {
            System.out.println("ID invalido. Por favor, ingrese un numero.");
            scanner.next();
            return;
        }
        Long id = scanner.nextLong();
        scanner.nextLine();

        Optional<Autor> existingAutor = autorDAO.findById(id);
        if (existingAutor.isPresent()) {
            Autor autorToUpdate = existingAutor.get();
            System.out.println("Autor actual: " + autorToUpdate);
            System.out.print("Nuevo Nombre (dejar vacio para mantener): ");
            String newNombre = scanner.nextLine();
            if (!newNombre.trim().isEmpty()) {
                autorToUpdate.setNombre(newNombre);
            }
            System.out.print("Nuevo Apellido (dejar vacio para mantener): ");
            String newApellido = scanner.nextLine();
            if (!newApellido.trim().isEmpty()) {
                autorToUpdate.setApellido(newApellido);
            }
            autorDAO.update(autorToUpdate);
            System.out.println("Autor actualizado exitosamente.");
        } else {
            System.out.println("No se encontro ningun autor con ID: " + id);
        }
    }

    private static void deleteAutor() throws SQLException {
        System.out.println("\n--- Eliminar Autor ---");
        System.out.print("Ingrese el ID del Autor a eliminar: ");
        if (!scanner.hasNextLong()) {
            System.out.println("ID invalido. Por favor, ingrese un numero.");
            scanner.next();
            return;
        }
        Long id = scanner.nextLong();
        scanner.nextLine();

        // Por simplicidad, aquí solo se intenta eliminar el autor.
        // Si hay una FK, la eliminación fallará si existen libros asociados.
        try {
            autorDAO.delete(id);
            System.out.println("Autor eliminado exitosamente si existia.");
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) { // SQLState para violacion de restriccion de integridad
                System.err.println("Error: No se puede eliminar el autor con ID " + id + " porque tiene libros asociados.");
                logger.warn("Intento de eliminar autor con libros asociados: {}", id, e);
            } else {
                throw e; //
            }
        }
    }

    // Metodos CRUD para Libro
    private static void createLibro() throws SQLException {
        System.out.println("\n--- Crear Nuevo Libro ---");
        System.out.print("Titulo del Libro: ");
        String titulo = scanner.nextLine();
        System.out.print("Anio de Publicacion: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Año de publicacion invalido. Por favor, ingrese un numero.");
            scanner.next();
            scanner.nextLine();
            return;
        }
        Integer anioPublicacion = scanner.nextInt();
        System.out.print("ID del Autor (debe existir): ");
        Long autorId = scanner.nextLong();
        scanner.nextLine(); // Consumir el salto de linea

        // Validacion basica de entrada
        if (titulo.trim().isEmpty()) {
            System.out.println("Error: El titulo no puede estar vacio.");
            return;
        }
        if (autorDAO.findById(autorId).isEmpty()) {
            System.out.println("Error: El ID del autor no existe. Por favor, cree el autor primero.");
            return;
        }

        Libro libro = new Libro();
        libro.setTitulo(titulo);
        libro.setAnioPublicacion(anioPublicacion);
        libro.setAutorId(autorId);
        libroDAO.save(libro);
        logger.info("Libro creado exitosamente con ID: {}", libro.getId());
    }

    private static void listLibros() throws SQLException {
        System.out.println("\n--- Lista de Libros ---");
        List<Libro> libros = libroDAO.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            libros.forEach(System.out::println);
        }
    }

    private static void findLibroById() throws SQLException {
        System.out.println("\n--- Buscar Libro por ID ---");
        System.out.print("Ingrese el ID del Libro a buscar: ");
        if (!scanner.hasNextLong()) {
            System.out.println("ID invalido. Por favor, ingrese un numero.");
            scanner.next();
            return;
        }
        Long id = scanner.nextLong();
        scanner.nextLine();

        Optional<Libro> libro = libroDAO.findById(id);
        if (libro.isPresent()) {
            System.out.println("Libro encontrado: " + libro.get());
        } else {
            System.out.println("No se encontro ningun libro con ID: " + id);
        }
    }

    private static void updateLibro() throws SQLException {
        System.out.println("\n--- Actualizar Libro ---");
        System.out.print("Ingrese el ID del Libro a actualizar: ");
        if (!scanner.hasNextLong()) {
            System.out.println("ID invalido. Por favor, ingrese un numero.");
            scanner.next();
            return;
        }
        Long id = scanner.nextLong();
        scanner.nextLine();

        Optional<Libro> existingLibro = libroDAO.findById(id);
        if (existingLibro.isPresent()) {
            Libro libroToUpdate = existingLibro.get();
            System.out.println("Libro actual: " + libroToUpdate);
            System.out.print("Nuevo Titulo (dejar vacio para mantener): ");
            String newTitulo = scanner.nextLine();
            if (!newTitulo.trim().isEmpty()) {
                libroToUpdate.setTitulo(newTitulo);
            }

            System.out.print("Nuevo Anio de Publicacion (dejar vacio para mantener, ingrese 0 para limpiar): ");
            String newAnioStr = scanner.nextLine();
            if (!newAnioStr.trim().isEmpty()) {
                try {
                    libroToUpdate.setAnioPublicacion(Integer.parseInt(newAnioStr));
                } catch (NumberFormatException e) {
                    System.out.println("Advertencia: Anio de publicacion invalido, se mantendra el actual.");
                }
            }

            System.out.print("Nuevo ID del Autor (dejar vacio para mantener, debe existir): ");
            String newAutorIdStr = scanner.nextLine();
            if (!newAutorIdStr.trim().isEmpty()) {
                try {
                    Long newAutorId = Long.parseLong(newAutorIdStr);
                    if (autorDAO.findById(newAutorId).isPresent()) {
                        libroToUpdate.setAutorId(newAutorId);
                    } else {
                        System.out.println("Advertencia: El nuevo ID de autor no existe, se mantendra el actual.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Advertencia: ID de autor invalido, se mantendra el actual.");
                }
            }

            libroDAO.update(libroToUpdate);
            System.out.println("Libro actualizado exitosamente.");
        } else {
            System.out.println("No se encontro ningun libro con ID: " + id);
        }
    }

    private static void deleteLibro() throws SQLException {
        System.out.println("\n--- Eliminar Libro ---");
        System.out.print("Ingrese el ID del Libro a eliminar: ");
        if (!scanner.hasNextLong()) {
            System.out.println("ID invalido. Por favor, ingrese un numero.");
            scanner.next();
            return;
        }
        Long id = scanner.nextLong();
        scanner.nextLine();

        libroDAO.delete(id);
        System.out.println("Libro eliminado exitosamente si existia.");
    }
}