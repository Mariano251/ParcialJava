package com.Peliculas.DAO.impl;

import com.Peliculas.DAO.DirectorDAO;
import com.Peliculas.model.Director;
import com.Peliculas.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DirectorDAOImpl implements DirectorDAO {
    private static final Logger logger = LoggerFactory.getLogger(DirectorDAOImpl.class);

    @Override
    public void save(Director director) throws SQLException {
        String sql = "INSERT INTO Directores (nombre, apellido) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, director.getNombre());
            stmt.setString(2, director.getApellido());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La creacion del director fallo, no se obtuvieron filas afectadas.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    director.setId(generatedKeys.getLong(1));
                    logger.info("Director guardado: {}", director);
                } else {
                    throw new SQLException("La creacion del director fallo, no se obtuvo ID generado.");
                }
            }
        }
    }

    @Override
    public Optional<Director> findById(Long id) throws SQLException {
        String sql = "SELECT id, nombre, apellido FROM Directores WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Director director = new Director(
                            rs.getLong("id"),
                            rs.getString("nombre"),
                            rs.getString("apellido")
                    );
                    logger.info("Director encontrado por ID: {}", director);
                    return Optional.of(director);
                }
            }
        }
        logger.info("Director con ID {} no encontrado.", id);
        return Optional.empty();
    }

    @Override
    public List<Director> findAll() throws SQLException {
        List<Director> directores = new ArrayList<>();
        String sql = "SELECT id, nombre, apellido FROM Directores";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                directores.add(new Director(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido")
                ));
            }
        }
        logger.info("Listando todos los directores. Total: {}", directores.size());
        return directores;
    }

    @Override
    public void update(Director director) throws SQLException {
        String sql = "UPDATE Directores SET nombre = ?, apellido = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, director.getNombre());
            stmt.setString(2, director.getApellido());
            stmt.setLong(3, director.getId());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Director actualizado: {}", director);
            } else {
                logger.warn("No se encontro director con ID {} para actualizar.", director.getId());
            }
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        // Considerar la eliminacion en cascada o la restriccion de FK para peliculas
        // Por simplicidad, asumimos que las peliculas asociadas se manejan por separado o se eliminan primero.
        String sql = "DELETE FROM Directores WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Director con ID {} eliminado.", id);
            } else {
                logger.warn("No se encontro director con ID {} para eliminar.", id);
            }
        }
    }
}