package com.Peliculas.DAO.impl;

import com.Peliculas.DAO.PeliculaDAO;
import com.Peliculas.model.Pelicula;
import com.Peliculas.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeliculaDAOImpl implements PeliculaDAO {
    private static final Logger logger = LoggerFactory.getLogger(PeliculaDAOImpl.class);

    @Override
    public void save(Pelicula pelicula) throws SQLException {
        String sql = "INSERT INTO Peliculas (titulo, anio_lanzamiento, director_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pelicula.getTitulo());
            stmt.setInt(2, pelicula.getAnioLanzamiento());
            stmt.setLong(3, pelicula.getDirectorId());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La creacion de la pelicula fallo, no se obtuvieron filas afectadas.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pelicula.setId(generatedKeys.getLong(1));
                    logger.info("Pelicula guardada: {}", pelicula);
                } else {
                    throw new SQLException("La creacion de la pelicula fallo, no se obtuvo ID generado.");
                }
            }
        }
    }

    @Override
    public Optional<Pelicula> findById(Long id) throws SQLException {
        // JOIN para obtener el nombre del director
        String sql = "SELECT p.id, p.titulo, p.anio_lanzamiento, p.director_id, " +
                "d.nombre, d.apellido " +
                "FROM Peliculas p JOIN Directores d ON p.director_id = d.id WHERE p.id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String directorNombreCompleto = rs.getString("nombre") + " " + rs.getString("apellido");
                    Pelicula pelicula = new Pelicula(
                            rs.getLong("id"),
                            rs.getString("titulo"),
                            rs.getInt("anio_lanzamiento"),
                            rs.getLong("director_id"),
                            directorNombreCompleto // Pasar el nombre completo
                    );
                    logger.info("Pelicula encontrada por ID: {}", pelicula);
                    return Optional.of(pelicula);
                }
            }
        }
        logger.info("Pelicula con ID {} no encontrada.", id);
        return Optional.empty();
    }

    @Override
    public List<Pelicula> findAll() throws SQLException {
        List<Pelicula> peliculas = new ArrayList<>();
        // Consulta SQL con JOIN para obtener el nombre y apellido del director
        String sql = "SELECT p.id, p.titulo, p.anio_lanzamiento, p.director_id, " +
                "d.nombre, d.apellido " +
                "FROM Peliculas p JOIN Directores d ON p.director_id = d.id";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String directorNombreCompleto = rs.getString("nombre") + " " + rs.getString("apellido");
                peliculas.add(new Pelicula(
                        rs.getLong("id"),
                        rs.getString("titulo"),
                        rs.getInt("anio_lanzamiento"),
                        rs.getLong("director_id"),
                        directorNombreCompleto // Pasar el nombre completo
                ));
            }
        }
        logger.info("Listando todas las peliculas. Total: {}", peliculas.size());
        return peliculas;
    }

    @Override
    public void update(Pelicula pelicula) throws SQLException {
        String sql = "UPDATE Peliculas SET titulo = ?, anio_lanzamiento = ?, director_id = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pelicula.getTitulo());
            stmt.setInt(2, pelicula.getAnioLanzamiento());
            stmt.setLong(3, pelicula.getDirectorId());
            stmt.setLong(4, pelicula.getId());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Pelicula actualizada: {}", pelicula);
            } else {
                logger.warn("No se encontro pelicula con ID {} para actualizar.", pelicula.getId());
            }
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM Peliculas WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Pelicula con ID {} eliminada.", id);
            } else {
                logger.warn("No se encontro pelicula con ID {} para eliminar.", id);
            }
        }
    }
}