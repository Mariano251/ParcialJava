package com.Peliculas.model;

public class Pelicula {
    private Long id;
    private String titulo;
    private Integer anioLanzamiento;
    private Long directorId;        // Clave foránea
    private String directorNombreCompleto; // Nuevo campo para el nombre completo del director

    public Pelicula() {}

    public Pelicula(Long id, String titulo, Integer anioLanzamiento, Long directorId) {
        this.id = id;
        this.titulo = titulo;
        this.anioLanzamiento = anioLanzamiento;
        this.directorId = directorId;
    }

    // Constructor adicional para cuando se obtenga el nombre completo del director
    public Pelicula(Long id, String titulo, Integer anioLanzamiento, Long directorId, String directorNombreCompleto) {
        this.id = id;
        this.titulo = titulo;
        this.anioLanzamiento = anioLanzamiento;
        this.directorId = directorId;
        this.directorNombreCompleto = directorNombreCompleto;
    }


    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public Integer getAnioLanzamiento() { return anioLanzamiento; }
    public void setAnioLanzamiento(Integer anioLanzamiento) { this.anioLanzamiento = anioLanzamiento; }
    public Long getDirectorId() { return directorId; }
    public void setDirectorId(Long directorId) { this.directorId = directorId; }

    // Nuevo Getter y Setter para directorNombreCompleto
    public String getDirectorNombreCompleto() { return directorNombreCompleto; }
    public void setDirectorNombreCompleto(String directorNombreCompleto) { this.directorNombreCompleto = directorNombreCompleto; }


    @Override
    public String toString() {
        // Modificado para mostrar el nombre completo del director si está disponible
        if (directorNombreCompleto != null && !directorNombreCompleto.isEmpty()) {
            return "Pelicula{" +
                    "id=" + id +
                    ", titulo='" + titulo + '\'' +
                    ", anioLanzamiento=" + anioLanzamiento +
                    ", director='" + directorNombreCompleto + '\'' + // Mostrar nombre completo
                    '}';
        } else {
            return "Pelicula{" +
                    "id=" + id +
                    ", titulo='" + titulo + '\'' +
                    ", anioLanzamiento=" + anioLanzamiento +
                    ", directorId=" + directorId +
                    '}';
        }
    }
}