-- Crear base de datos (se creará automáticamente al conectar)
-- Tabla Estudiantes
CREATE TABLE IF NOT EXISTS estudiantes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombreCompleto TEXT NOT NULL,
    fechaNacimiento DATE NOT NULL,
    cedula TEXT NOT NULL UNIQUE,
    grado INTEGER NOT NULL DEFAULT 1,
    seccion TEXT NOT NULL
);

-- Tabla Materias
CREATE TABLE IF NOT EXISTS materias (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    descripcion TEXT NOT NULL,
    profesor TEXT NOT NULL,
    grado INTEGER NOT NULL DEFAULT 1
);

-- Tabla Notas
CREATE TABLE IF NOT EXISTS notas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    estudiante_id INTEGER NOT NULL,
    materia_id INTEGER NOT NULL,
    calificacion REAL NOT NULL CHECK (calificacion BETWEEN 0 AND 20),
    fecha_registro DATE NOT NULL DEFAULT CURRENT_DATE,
    anio_escolar INTEGER NOT NULL,
    FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id) ON DELETE CASCADE,
    FOREIGN KEY (materia_id) REFERENCES materias(id)
);
