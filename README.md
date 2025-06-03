Sistema de Gestión de Películas (Java con JDBC y H2)
Este proyecto es una aplicación de consola en Java que permite gestionar la información de directores de películas y películas, persistiendo los datos en una base de datos relacional H2. Está diseñado siguiendo un patrón de arquitectura por capas con Data Access Objects (DAO).

1. Características Principales
Gestión de Entidades (CRUD): Permite realizar operaciones completas de Crear, Leer (Listar y Buscar por ID), Actualizar y Eliminar (CRUD) sobre dos entidades principales: Directores y Peliculas.
Base de Datos H2: Utiliza la base de datos relacional H2 en modo archivo (./data/peliculas_db), asegurando que los datos persistan entre ejecuciones de la aplicación.
JDBC: La comunicación con la base de datos se realiza a través de la API estándar de Java Database Connectivity (JDBC).
Patrón DAO: Implementación robusta del patrón Data Access Object para una clara separación entre la lógica de acceso a datos y la lógica de negocio. Se utiliza una interfaz DAO genérica (GenericDAO<T, ID>) y sus implementaciones específicas por entidad (DirectorDAOImpl, PeliculaDAOImpl).
Gradle: El proyecto se construye y gestiona completamente utilizando Gradle, facilitando la gestión de dependencias y la automatización de tareas.
Manejo de Excepciones: Implementación de manejo adecuado de excepciones (SQLException) y uso de try-with-resources para la gestión automática de recursos JDBC.
Logging: Integración de la API SLF4J con Log4j2 como implementación para el registro de eventos y errores, tanto en consola como en un archivo de log.
Validación Básica de Entrada: Se incluyen validaciones simples para las entradas del usuario desde la consola (ej. no permitir campos vacíos, validar IDs).
Relación entre Entidades: Las entidades Director y Pelicula están relacionadas (un director puede tener múltiples películas) a través de una clave foránea en la tabla Peliculas. Al listar películas, se muestra el nombre completo del director asociado en lugar de solo su ID, mejorando la legibilidad.
2. Estructura del Proyecto
La organización del código sigue una estructura de capas clara para una mejor modularidad y mantenimiento:
```

├── .gradle/                  # Archivos internos de Gradle (generados automáticamente)
├── build/                    # Directorio de salida de la compilación y empaquetado (generado por Gradle)
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── data/                     # Directorio para los archivos de la base de datos H2 (generado en tiempo de ejecución)
│   └── peliculas_db.mv.db
│   └── peliculas_db.trace.db
├── logs/                     # Directorio para los archivos de log (generado en tiempo de ejecución)
│   └── application.log
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── Peliculas/
│       │           ├── DAO/
│       │           │   ├── GenericDAO.java   # Interfaz genérica para operaciones CRUD
│       │           │   ├── DirectorDAO.java  # Interfaz DAO para directores
│       │           │   └── PeliculaDAO.java  # Interfaz DAO para películas
│       │           ├── DAO/impl/
│       │           │   ├── DirectorDAOImpl.java # Implementación DAO para directores
│       │           │   └── PeliculaDAOImpl.java # Implementación DAO para películas
│       │           ├── model/
│       │           │   ├── Director.java       # Clase de modelo para la entidad Director
│       │           │   └── Pelicula.java       # Clase de modelo para la entidad Pelicula
│       │           ├── main/
│       │           │   └── mainApp.java        # Clase principal con el menú de la aplicación
│       │           └── util/
│       │               └── DatabaseUtil.java # Clase de utilidad para la conexión y setup de la DB
│       └── resources/
│           └── log4j2.xml      # Configuración para el sistema de logging Log4j2
├── .gitignore                # Archivo para ignorar archivos y directorios en Git
├── build.gradle              # Script de construcción de Gradle (define dependencias, tareas, etc.)
├── gradlew                   # Script de la envoltura de Gradle (para ejecutar comandos Gradle)
├── gradlew.bat               # Script de la envoltura de Gradle para Windows
└── settings.gradle           # Configuración básica de Gradle para el proyecto
```

3. Requisitos Previos
Asegúrate de tener instalado lo siguiente en tu sistema:

Java Development Kit (JDK) 11 o superior.
Gradle: No es necesario instalarlo globalmente si usas gradlew (Gradle Wrapper), que se incluye en el proyecto.

4. Cómo Ejecutar el Proyecto
Sigue estos pasos para compilar y ejecutar la aplicación de consola:

Navega a la Carpeta Raíz del Proyecto:
Abre una terminal (Símbolo del sistema, PowerShell, o Terminal en macOS/Linux) y navega hasta el directorio donde se encuentran los archivos build.gradle, gradlew y gradlew.bat.
Por ejemplo:

Bash

cd C:\ruta\a\tu\proyecto\MiProyectoPeliculas # En Windows
# o
cd /ruta/a/tu/proyecto/MiProyectoPeliculas # En macOS/Linux

Ejecuta la Aplicación:
Utiliza el siguiente comando de Gradle para compilar y ejecutar la aplicación:

En Windows:
Bash

.\gradlew run

En macOS/Linux:
Bash

./gradlew run

La primera vez que ejecutes, Gradle descargará las dependencias y compilará el código, lo que puede tomar un tiempo. Las ejecuciones posteriores serán más rápidas.

Interactúa con la Aplicación: Una vez que la aplicación se inicie, se presentará un menú en la consola. Sigue las instrucciones para realizar operaciones CRUD sobre directores y películas.

5. Base de Datos y Persistencia
La base de datos H2 se creará automáticamente la primera vez que se ejecute la aplicación. Los archivos de la base de datos (peliculas_db.mv.db y peliculas_db.trace.db) se almacenarán en una nueva carpeta llamada data en la raíz de tu proyecto.
No se requiere un script SQL externo, ya que la creación de las tablas (Directores y Peliculas) se maneja programáticamente en el método DatabaseUtil.initializeDatabase() al inicio de la aplicación.

6. Configuración de Logging (Log4j2)
La aplicación utiliza SLF4J como fachada y Log4j2 como la implementación de logging. La configuración se encuentra en el archivo src/main/resources/log4j2.xml, que define los appenders para la consola y para un archivo de log llamado application.log (ubicado en la carpeta logs/ dentro de la raíz del proyecto).

Desarrollado por: [Mariano Lopez Tubaro]
Fecha: [03/06/2025]
