# Midaxus - Plataforma de Gestión Académica

Midaxus es una plataforma de gestión académica moderna diseñada para optimizar los flujos de trabajo institucionales. Proporciona un centro de control unificado para la gestión de horarios, inscripciones, visualización de datos de estudiantes y comunicación docente.

## 🚀 Características Principales

*   **Gestión de Usuarios Multi-rol:** Soporte para Estudiantes, Profesores y Administradores.
*   **Gestión de Horarios:** Sistema inteligente para la asignación y consulta de horarios académicos.
*   **Inscripciones y Matrículas:** Proceso simplificado para el registro de estudiantes en cursos y grupos.
*   **Asistencia y Seguimiento:** Control detallado de la asistencia por curso y fecha.
*   **Seguridad Robusta:** Autenticación y autorización basada en JWT (JSON Web Tokens).
*   **Arquitectura Limpia:** Código organizado siguiendo los estándares de Google Java Style.

## 🛠️ Stack Tecnológico

*   **Lenguaje:** Java 21
*   **Framework:** Spring Boot 3.3.2
*   **Persistencia:** Spring Data JPA / Hibernate
*   **Base de Datos:** MySQL 8.4
*   **Seguridad:** Spring Security + JWT
*   **Documentación de API:** Javadoc integral
*   **Calidad de Código:** Maven Checkstyle (Google Style) + SonarQube Ready
*   **Infraestructura:** Docker & Docker Compose

## 📋 Requisitos Previos

*   Java 21 JDK o superior
*   Maven 3.6+
*   Docker y Docker Compose (opcional para base de datos)
*   MySQL 8.x

## 🔧 Configuración e Instalación

1.  **Clonar el repositorio:**
    ```bash
    git clone https://github.com/Danieel07/Midaxus-1.0.git
    cd Midaxus-1.0
    ```

2.  **Configurar Variables de Entorno:**
    Crea un archivo `.env` o configura las propiedades en `src/main/resources/application.properties` con tus credenciales de base de datos y SMTP.

3.  **Levantar la Base de Datos (Docker):**
    ```bash
    docker-compose up -d
    ```

4.  **Compilar y Ejecutar:**
    ```bash
    ./mvnw clean install
    ./mvnw spring-boot:run
    ```

## 💎 Estándares de Calidad

El proyecto se rige por la **Google Java Style Guide**. Para verificar el cumplimiento del linter, ejecuta:

```bash
./mvnw checkstyle:check
```

## 📊 Diagramas de Diseño

*   [Diagrama EMR (Entidad-Relación)](https://lucid.app/lucidchart/bd115058-8b4d-4485-a6a7-bde4ce42f5ab/edit?viewport_loc=284%2C-2461%2C4957%2C2963%2C0_0&invitationId=inv_d94c456e-99ff-436c-8d8f-e7b262d7e0ec)
*   [Diagrama UML de Clases](https://lucid.app/lucidchart/c1e052a3-9348-4085-9b95-88ceeaddd63c/edit?viewport_loc=-250%2C-1372%2C4892%2C2924%2C0_0&invitationId=inv_4ae190ca-4e71-47d7-b6bb-10aa455fd52d)

## 📄 Licencia

Este proyecto es propiedad privada. Todos los derechos reservados.

---
*Desarrollado con precisión y minimalismo para una gestión académica experta.*
