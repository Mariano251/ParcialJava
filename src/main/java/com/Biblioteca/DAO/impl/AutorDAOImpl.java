package com.Biblioteca.DAO.impl;

import com.Biblioteca.DAO.AutorDAO;
import com.Biblioteca.model.Autor;
import com.Biblioteca.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutorDAOImpl implements AutorDAO {
    private static final Logger logger = LoggerFactory.getLogger(AutorDAOImpl.class);

    @Override
    public void save(Autor autor) throws SQLException {
        String sql = "INSERT INTO Autores (nombre, apellido) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, autor.getNombre());
            stmt.setString(2, autor.getApellido());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La creacion del autor fallo, no se obtuvieron filas afectadas.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    autor.setId(generatedKeys.getLong(1));
                    logger.info("Autor guardado: {}", autor);
                } else {
                    throw new SQLException("La creacion del autor fallo, no se obtuvo ID generado.");
                }
            }
        }
    }

    @Override
    public Optional<Autor> findById(Long id) throws SQLException {
        String sql = "SELECT id, nombre, apellido FROM Autores WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Autor autor = new Autor(
                            rs.getLong("id"),
                            rs.getString("nombre"),
                            rs.getString("apellido")
                    );
                    logger.info("Autor encontrado por ID: {}", autor);
                    return Optional.of(autor);
                }
            }
        }
        logger.info("Autor con ID {} no encontrado.", id);
        return Optional.empty();
    }

    @Override
    public List<Autor> findAll() throws SQLException {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT id, nombre, apellido FROM Autores";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                autores.add(new Autor(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido")
                ));
            }
        }
        logger.info("Listando todos los autores. Total: {}", autores.size());
        return autores;
    }

    @Override
    public void update(Autor autor) throws SQLException {
        String sql = "UPDATE Autores SET nombre = ?, apellido = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, autor.getNombre());
            stmt.setString(2, autor.getApellido());
            stmt.setLong(3, autor.getId());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Autor actualizado: {}", autor);
            } else {
                logger.warn("No se encontro autor con ID {} para actualizar.", autor.getId());
            }
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        // Considerar la eliminación en cascada o la restricción de FK para libros
        // Por simplicidad, asumimos que los libros asociados se manejan por separado o se eliminan primero.
        String sql = "DELETE FROM Autores WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Autor con ID {} eliminado.", id);
            } else {
                logger.warn("No se encontro autor con ID {} para eliminar.", id);
            }
        }
    }
}
