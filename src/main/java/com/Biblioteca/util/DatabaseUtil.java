package com.Biblioteca.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseUtil {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);
    private static final String JDBC_URL = "jdbc:h2:./data/biblioteca"; // Modo archivo
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        logger.info("Estableciendo conexion a la base de datos...");
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Crear tabla Autor
            String createAutorTableSQL = "CREATE TABLE IF NOT EXISTS Autores (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "nombre VARCHAR(255) NOT NULL," +
                    "apellido VARCHAR(255) NOT NULL" +
                    ");";
            stmt.execute(createAutorTableSQL);
            logger.info("Tabla 'Autores' creada o ya existe.");

            // Crear tabla Libro
            String createLibroTableSQL = "CREATE TABLE IF NOT EXISTS Libros (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "titulo VARCHAR(255) NOT NULL," +
                    "anio_publicacion INT," +
                    "autor_id INT NOT NULL," +
                    "FOREIGN KEY (autor_id) REFERENCES Autores(id)" +
                    ");";
            stmt.execute(createLibroTableSQL);
            logger.info("Tabla 'Libros' creada o ya existe.");

        } catch (SQLException e) {
            logger.error("Error al inicializar la base de datos: {}", e.getMessage(), e);
        }
    }

    //No utilizo un metodo para cerrar recursos porque try-with-resources lo hace por nosotros

}
