package com.Peliculas.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseUtil {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);
    private static final String JDBC_URL = "jdbc:h2:./data/peliculas_db"; // Cambiado el nombre del archivo de la DB
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        logger.info("Estableciendo conexion a la base de datos...");
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Crear tabla Directores
            String createDirectorTableSQL = "CREATE TABLE IF NOT EXISTS Directores (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "nombre VARCHAR(255) NOT NULL," +
                    "apellido VARCHAR(255) NOT NULL" +
                    ");";
            stmt.execute(createDirectorTableSQL);
            logger.info("Tabla 'Directores' creada o ya existe.");

            // Crear tabla Peliculas
            String createPeliculaTableSQL = "CREATE TABLE IF NOT EXISTS Peliculas (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "titulo VARCHAR(255) NOT NULL," +
                    "anio_lanzamiento INT," + // Renombrado de anio_publicacion
                    "director_id INT NOT NULL," + // Renombrado de autor_id
                    "FOREIGN KEY (director_id) REFERENCES Directores(id)" +
                    ");";
            stmt.execute(createPeliculaTableSQL);
            logger.info("Tabla 'Peliculas' creada o ya existe.");

        } catch (SQLException e) {
            logger.error("Error al inicializar la base de datos: {}", e.getMessage(), e);
        }
    }

    // No utilizo un metodo para cerrar recursos porque try-with-resources lo hace por nosotros

}