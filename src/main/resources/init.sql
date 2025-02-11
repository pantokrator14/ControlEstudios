-- Crear base de datos (se creará automáticamente al conectar)
-- Tabla Estudiantes
CREATE TABLE IF NOT EXISTS estudiantes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_completo TEXT NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    cedula TEXT NOT NULL UNIQUE,
    seccion TEXT NOT NULL
);

-- Tabla Materias
CREATE TABLE IF NOT EXISTS materias (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    descripcion TEXT NOT NULL,
    nombre_profesor TEXT NOT NULL
);

-- Tabla Notas
CREATE TABLE IF NOT EXISTS notas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    estudiante_id INTEGER NOT NULL,
    materia_id INTEGER NOT NULL,
    calificacion REAL NOT NULL CHECK (calificacion BETWEEN 0 AND 20),
    FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id) ON DELETE CASCADE,
    FOREIGN KEY (materia_id) REFERENCES materias(id)
);