# 🎓 Control de Estudios - JavaFX

![Java](https://img.shields.io/badge/Java-21-blue)
![JavaFX](https://img.shields.io/badge/JavaFX-21-orange)
![SQLite](https://img.shields.io/badge/SQLite-3.43-lightgrey)
![Maven](https://img.shields.io/badge/Maven-3.8.6-red)

Aplicación de escritorio para gestión académica con capacidad de generar reportes en PDF. Desarrollada para la Unidad Educativa Nacional "César Augusto Agreda".

## 🌟 Características Principales
- **Gestión de Estudiantes**: Registro de datos personales y secciones.
- **Administración de Materias**: Creación de asignaturas con profesores responsables.
- **Registro de Notas**: Asignación de calificaciones por materia.
- **Generación de PDF**: Boletines con promedios y firmas digitalizadas.
- **Base de Datos Local**: Almacenamiento persistente con SQLite.


## 🛠️ Prerrequisitos
- JDK 21+ ([Azul Zulu FX recomendado](https://www.azul.com/downloads/?package=jdk-fx))
- Maven 3.8+
- Sistema operativo Windows 7/8/8.1/10/11

## ⚙️ Instalación
1. Clona el repositorio:
   ```bash
   git clone https://github.com/tuusuario/control-estudios.git
2. Compila el proyecto:
  ```bash
   mvn clean package
   ```
3. Genera el ejecutable:
    ```bash
    mvn jpackage:jpackage
    ```
El instalador se encontrará en target/dist/ControlEstudios.exe

📂 Estructura del Proyecto

ControlEstudios/
├── src/
│   ├── main/
│   │   ├── java/          # Lógica de la aplicación
│   │   └── resources/     # Recursos (FXML, imágenes, SQL)
├── target/                # Artefactos generados
└── pom.xml                # Configuración de Maven

🖥️ Uso Básico

    Inicio de Sesión:

        Usuario: admin

        Contraseña: admin

    Navega entre módulos usando el menú lateral.

    Genera boletines y constancias en PDF desde el módulo de notas.

🔧 Tecnologías Clave

    JavaFX 21: Interfaz gráfica moderna

    SQLite: Base de datos embebida

    Apache PDFBox: Generación de documentos PDF

    Ikonli: Iconos profesionales (Material Design)

❓ Preguntas Frecuentes

¿Dónde se almacenan los datos?
En C:/Users/[tu_usuario]/.ControlEstudios/database.db

¿Cómo cambiar el logo del PDF?
Reemplaza src/main/resources/images/logo.png manteniendo el mismo nombre.

¿Cómo crear usuarios nuevos?
Actualmente solo soporta el usuario admin (próxima versión incluirá gestión de usuarios).


📬 Contacto
📧 julioblogs1998@gmail.com
💼 linkedin.com/in/juliusjosepham
