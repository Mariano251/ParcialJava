package com.Biblioteca.DAO.impl;

import com.Biblioteca.DAO.LibroDAO;
import com.Biblioteca.model.Libro;
import com.Biblioteca.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibroDAOImpl implements LibroDAO {
    private static final Logger logger = LoggerFactory.getLogger(LibroDAOImpl.class);

    @Override
    public void save(Libro libro) throws SQLException {
        String sql = "INSERT INTO Libros (titulo, anio_publicacion, autor_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, libro.getTitulo());
            stmt.setInt(2, libro.getAnioPublicacion());
            stmt.setLong(3, libro.getAutorId());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La creacion del libro fallo, no se obtuvieron filas afectadas.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    libro.setId(generatedKeys.getLong(1));
                    logger.info("Libro guardado: {}", libro);
                } else {
                    throw new SQLException("La creacion del libro fallo, no se obtuvo ID generado.");
                }
            }
        }
    }

    @Override
    public Optional<Libro> findById(Long id) throws SQLException {
        String sql = "SELECT id, titulo, anio_publicacion, autor_id FROM Libros WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Libro libro = new Libro(
                            rs.getLong("id"),
                            rs.getString("titulo"),
                            rs.getInt("anio_publicacion"),
                            rs.getLong("autor_id")
                    );
                    logger.info("Libro encontrado por ID: {}", libro);
                    return Optional.of(libro);
                }
            }
        }
        logger.info("Libro con ID {} no encontrado.", id);
        return Optional.empty();
    }

    @Override
    public List<Libro> findAll() throws SQLException {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT id, titulo, anio_publicacion, autor_id FROM Libros";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                libros.add(new Libro(
                        rs.getLong("id"),
                        rs.getString("titulo"),
                        rs.getInt("anio_publicacion"),
                        rs.getLong("autor_id")
                ));
            }
        }
        logger.info("Listando todos los libros. Total: {}", libros.size());
        return libros;
    }

    @Override
    public void update(Libro libro) throws SQLException {
        String sql = "UPDATE Libros SET titulo = ?, anio_publicacion = ?, autor_id = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, libro.getTitulo());
            stmt.setInt(2, libro.getAnioPublicacion());
            stmt.setLong(3, libro.getAutorId());
            stmt.setLong(4, libro.getId());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Libro actualizado: {}", libro);
            } else {
                logger.warn("No se encontro libro con ID {} para actualizar.", libro.getId());
            }
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM Libros WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Libro con ID {} eliminado.", id);
            } else {
                logger.warn("No se encontro libro con ID {} para eliminar.", id);
            }
        }
    }
}