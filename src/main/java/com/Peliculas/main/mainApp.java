package com.Peliculas.main;

import com.Peliculas.DAO.DirectorDAO;
import com.Peliculas.DAO.PeliculaDAO;
import com.Peliculas.DAO.impl.DirectorDAOImpl;
import com.Peliculas.DAO.impl.PeliculaDAOImpl;
import com.Peliculas.model.Director;
import com.Peliculas.model.Pelicula;
import com.Peliculas.util.DatabaseUtil;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class mainApp {
    private static final Logger logger = LoggerFactory.getLogger(mainApp.class);
    private static final Scanner scanner = new Scanner(System.in);
    private static final DirectorDAO directorDAO = new DirectorDAOImpl();
    private static final PeliculaDAO peliculaDAO = new PeliculaDAOImpl();

    public static void main(String[] args) {
        DatabaseUtil.initializeDatabase(); // Asegura que las tablas existan

        System.out.println("¡Bienvenido al sistema de gestion de Peliculas!");
        int option;
        do {
            displayMenu();
            option = getUserOption();

            try {
                switch (option) {
                    case 1:
                        createDirector();
                        break;
                    case 2:
                        listDirectores();
                        break;
                    case 3:
                        findDirectorById();
                        break;
                    case 4:
                        updateDirector();
                        break;
                    case 5:
                        deleteDirector();
                        break;
                    case 6:
                        createPelicula();
                        break;
                    case 7:
                        listPeliculas();
                        break;
                    case 8:
                        findPeliculaById();
                        break;
                    case 9:
                        updatePelicula();
                        break;
                    case 10:
                        deletePelicula();
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
        System.out.println("Gestion de Directores:");
        System.out.println("  1. Crear Director");
        System.out.println("  2. Listar Directores");
        System.out.println("  3. Buscar Director por ID");
        System.out.println("  4. Actualizar Director");
        System.out.println("  5. Eliminar Director");
        System.out.println("Gestion de Peliculas:");
        System.out.println("  6. Crear Pelicula");
        System.out.println("  7. Listar Peliculas");
        System.out.println("  8. Buscar Pelicula por ID");
        System.out.println("  9. Actualizar Pelicula");
        System.out.println("  10. Eliminar Pelicula");
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

    // Metodos CRUD para Director
    private static void createDirector() throws SQLException {
        System.out.println("\n--- Crear Nuevo Director ---");
        System.out.print("Nombre del Director: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido del Director: ");
        String apellido = scanner.nextLine();

        // Validacion basica de entrada
        if (nombre.trim().isEmpty() || apellido.trim().isEmpty()) {
            System.out.println("Error: Nombre y apellido no pueden estar vacios.");
            return;
        }

        Director director = new Director();
        director.setNombre(nombre);
        director.setApellido(apellido);
        directorDAO.save(director);
        System.out.println("Director creado exitosamente con ID: " + director.getId());
    }

    private static void listDirectores() throws SQLException {
        System.out.println("\n--- Lista de Directores ---");
        List<Director> directores = directorDAO.findAll();
        if (directores.isEmpty()) {
            System.out.println("No hay directores registrados.");
        } else {
            directores.forEach(System.out::println);
        }
    }

    private static void findDirectorById() throws SQLException {
        System.out.println("\n--- Buscar Director por ID ---");
        System.out.print("Ingrese el ID del Director a buscar: ");
        if (!scanner.hasNextLong()) {
            System.out.println("ID invalido. Por favor, ingrese un numero.");
            scanner.next();
            return;
        }
        Long id = scanner.nextLong();
        scanner.nextLine();

        Optional<Director> director = directorDAO.findById(id);
        if (director.isPresent()) {
            System.out.println("Director encontrado: " + director.get());
        } else {
            System.out.println("No se encontro ningun director con ID: " + id);
        }
    }

    private static void updateDirector() throws SQLException {
        System.out.println("\n--- Actualizar Director ---");
        System.out.print("Ingrese el ID del Director a actualizar: ");
        if (!scanner.hasNextLong()) {
            System.out.println("ID invalido. Por favor, ingrese un numero.");
            scanner.next();
            return;
        }
        Long id = scanner.nextLong();
        scanner.nextLine();

        Optional<Director> existingDirector = directorDAO.findById(id);
        if (existingDirector.isPresent()) {
            Director directorToUpdate = existingDirector.get();
            System.out.println("Director actual: " + directorToUpdate);
            System.out.print("Nuevo Nombre (dejar vacio para mantener): ");
            String newNombre = scanner.nextLine();
            if (!newNombre.trim().isEmpty()) {
                directorToUpdate.setNombre(newNombre);
            }
            System.out.print("Nuevo Apellido (dejar vacio para mantener): ");
            String newApellido = scanner.nextLine();
            if (!newApellido.trim().isEmpty()) {
                directorToUpdate.setApellido(newApellido);
            }
            directorDAO.update(directorToUpdate);
            System.out.println("Director actualizado exitosamente.");
        } else {
            System.out.println("No se encontro ningun director con ID: " + id);
        }
    }

    private static void deleteDirector() throws SQLException {
        System.out.println("\n--- Eliminar Director ---");
        System.out.print("Ingrese el ID del Director a eliminar: ");
        if (!scanner.hasNextLong()) {
            System.out.println("ID invalido. Por favor, ingrese un numero.");
            scanner.next();
            return;
        }
        Long id = scanner.nextLong();
        scanner.nextLine();

        // Por simplicidad, aquí solo se intenta eliminar el director.
        // Si hay una FK, la eliminacion fallara si existen peliculas asociadas.
        try {
            directorDAO.delete(id);
            System.out.println("Director eliminado exitosamente si existia.");
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) { // SQLState para violacion de restriccion de integridad
                System.err.println("Error: No se puede eliminar el director con ID " + id + " porque tiene peliculas asociadas.");
                logger.warn("Intento de eliminar director con peliculas asociadas: {}", id, e);
            } else {
                throw e; //
            }
        }
    }

    // Metodos CRUD para Pelicula
    private static void createPelicula() throws SQLException {
        System.out.println("\n--- Crear Nueva Pelicula ---");
        System.out.print("Titulo de la Pelicula: ");
        String titulo = scanner.nextLine();
        System.out.print("Año de Lanzamiento: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Año de lanzamiento invalido. Por favor, ingrese un numero.");
            scanner.next();
            scanner.nextLine();
            return;
        }
        Integer anioLanzamiento = scanner.nextInt();
        System.out.print("ID del Director (debe existir): ");
        Long directorId = scanner.nextLong();
        scanner.nextLine(); // Consumir el salto de linea

        // Validacion basica de entrada
        if (titulo.trim().isEmpty()) {
            System.out.println("Error: El titulo no puede estar vacio.");
            return;
        }
        if (directorDAO.findById(directorId).isEmpty()) {
            System.out.println("Error: El ID del director no existe. Por favor, cree el director primero.");
            return;
        }

        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo(titulo);
        pelicula.setAnioLanzamiento(anioLanzamiento);
        pelicula.setDirectorId(directorId);
        peliculaDAO.save(pelicula);
        logger.info("Pelicula creada exitosamente con ID: {}", pelicula.getId());
    }

    private static void listPeliculas() throws SQLException {
        System.out.println("\n--- Lista de Peliculas ---");
        List<Pelicula> peliculas = peliculaDAO.findAll();
        if (peliculas.isEmpty()) {
            System.out.println("No hay peliculas registradas.");
        } else {
            peliculas.forEach(System.out::println);
        }
    }

    private static void findPeliculaById() throws SQLException {
        System.out.println("\n--- Buscar Pelicula por ID ---");
        System.out.print("Ingrese el ID de la Pelicula a buscar: ");
        if (!scanner.hasNextLong()) {
            System.out.println("ID invalido. Por favor, ingrese un numero.");
            scanner.next();
            return;
        }
        Long id = scanner.nextLong();
        scanner.nextLine();

        Optional<Pelicula> pelicula = peliculaDAO.findById(id);
        if (pelicula.isPresent()) {
            System.out.println("Pelicula encontrada: " + pelicula.get());
        } else {
            System.out.println("No se encontro ninguna pelicula con ID: " + id);
        }
    }

    private static void updatePelicula() throws SQLException {
        System.out.println("\n--- Actualizar Pelicula ---");
        System.out.print("Ingrese el ID de la Pelicula a actualizar: ");
        if (!scanner.hasNextLong()) {
            System.out.println("ID invalido. Por favor, ingrese un numero.");
            scanner.next();
            return;
        }
        Long id = scanner.nextLong();
        scanner.nextLine();

        Optional<Pelicula> existingPelicula = peliculaDAO.findById(id);
        if (existingPelicula.isPresent()) {
            Pelicula peliculaToUpdate = existingPelicula.get();
            System.out.println("Pelicula actual: " + peliculaToUpdate);
            System.out.print("Nuevo Titulo (dejar vacio para mantener): ");
            String newTitulo = scanner.nextLine();
            if (!newTitulo.trim().isEmpty()) {
                peliculaToUpdate.setTitulo(newTitulo);
            }

            System.out.print("Nuevo Año de Lanzamiento (dejar vacio para mantener, ingrese 0 para limpiar): ");
            String newAnioStr = scanner.nextLine();
            if (!newAnioStr.trim().isEmpty()) {
                try {
                    peliculaToUpdate.setAnioLanzamiento(Integer.parseInt(newAnioStr));
                } catch (NumberFormatException e) {
                    System.out.println("Advertencia: Año de lanzamiento invalido, se mantendra el actual.");
                }
            }

            System.out.print("Nuevo ID del Director (dejar vacio para mantener, debe existir): ");
            String newDirectorIdStr = scanner.nextLine();
            if (!newDirectorIdStr.trim().isEmpty()) {
                try {
                    Long newDirectorId = Long.parseLong(newDirectorIdStr);
                    if (directorDAO.findById(newDirectorId).isPresent()) {
                        peliculaToUpdate.setDirectorId(newDirectorId);
                    } else {
                        System.out.println("Advertencia: El nuevo ID de director no existe, se mantendra el actual.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Advertencia: ID de director invalido, se mantendra el actual.");
                }
            }

            peliculaDAO.update(peliculaToUpdate);
            System.out.println("Pelicula actualizada exitosamente.");
        } else {
            System.out.println("No se encontro ninguna pelicula con ID: " + id);
        }
    }

    private static void deletePelicula() throws SQLException {
        System.out.println("\n--- Eliminar Pelicula ---");
        System.out.print("Ingrese el ID de la Pelicula a eliminar: ");
        if (!scanner.hasNextLong()) {
            System.out.println("ID invalido. Por favor, ingrese un numero.");
            scanner.next();
            return;
        }
        Long id = scanner.nextLong();
        scanner.nextLine();

        peliculaDAO.delete(id);
        System.out.println("Pelicula eliminada exitosamente si existia.");
    }
}
