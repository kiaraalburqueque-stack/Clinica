# Clínica — Módulo de enfermería

Proyecto académico funcional con Spring Boot 3.5.6, Java 17 o superior, Thymeleaf, Spring Security, JPA/Hibernate y base de datos H2 persistente. Incluye perfil MySQL opcional.

## Ejecutar en Windows

Desde la raíz del proyecto en PowerShell:

```powershell
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

Abre http://localhost:8080. El script utiliza Maven instalado o el caché local; si no existe, descarga Maven y verifica su checksum. Las dependencias se guardan dentro de `.tools/m2`. La primera compilación necesita internet si faltan dependencias.

| Usuario | Contraseña de demostración | Acceso |
|---|---|---|
| ana | Demo123! | Juan y María |
| elena | Demo123! | Luis |
| supervisor | Demo123! | Todos los pacientes; seleccionar profesional en asignaciones |
| admin | Demo123! | Administración de usuarios, roles, auditoría y módulos existentes |
| medico | Demo123! | Consulta de pacientes e historiales clínicos; sin escritura de enfermería |
| recepcion | Demo123! | Consulta de pacientes y datos personales; sin información clínica |

Todos los datos son ficticios. Las asignaciones iniciales cubren el día del primer arranque y los 13 siguientes. Para una fecha posterior utiliza el filtro de fecha para consultar las asignaciones originales. Los datos demo se cargan una sola vez; los reinicios no duplican los pacientes ni las asignaciones. El arranque migra los profesionales existentes a cuentas de usuario conservando sus IDs y hashes de contraseña. Cambios posteriores de usuarios y roles se conservan al reiniciar.

## Mejoras de la evaluación 2

- Gestión completa de usuarios y roles en `/admin/usuarios` y `/admin/roles`: registro, listado, edición y activación/desactivación.
- Relación `Usuario`–`Profesional` con `OneToOne`, `Usuario`–`Rol` con `ManyToMany`, además de las relaciones `OneToMany` y `ManyToOne` clínicas.
- Un usuario puede tener uno o varios roles. Cada rol tiene permisos y estado propios.
- Auditoría automática mediante `AuditAspect` y `@Auditable`: registra las escrituras clínicas y administrativas dentro de la misma transacción. Los cambios fallidos no generan auditorías de éxito.
- Bitácora consultable en `/auditoria`, con filtros y datos anteriores a modificaciones. No registra contraseñas ni hashes de cuenta en las instantáneas.
- Autorización en rutas y servicios, menú según permisos y redirección después del login. Los cambios de estado o permisos se aplican a sesiones existentes en la siguiente petición.
- Validación de entradas, control de versión, conflictos de integridad y manejo global de errores de API e interfaz.

La desactivación cumple la eliminación lógica de usuarios y roles; no rompe sus relaciones. El sistema impide dejarse sin ningún administrador activo. El médico tiene consulta clínica; el recepcionista solo datos personales. El módulo de citas no pertenece al alcance de los ocho requerimientos originales.

Consulta [la correspondencia con la evaluación y la guía de demostración](docs/Evaluacion-02.md).

## Requerimientos implementados

| Persona | Funciones |
|---|---|
| 1 | RF-ENF-10: registrar signos vitales; RF-ENF-12: historial |
| 2 | RF-ENF-07: registrar valoración; RF-ENF-09: historial |
| 3 | RF-ENF-25: registrar procedimientos; RF-ENF-27: historial |
| 4 | RF-ENF-31: registrar notas; RF-ENF-06: consultar pacientes asignados |

Los formularios validan datos en el servidor. Los historiales tienen filtros por fechas, orden descendente y paginación; procedimientos permite filtrar por tipo. Cada registro conserva fecha, hora de Lima y autor autenticado. Los signos vitales aceptan mediciones parciales y exigen los dos componentes de presión arterial si se ingresa uno.

También incluye corrección con motivo, control de versión para evitar sobrescrituras simultáneas y anulación lógica con motivo. Estas operaciones complementan el CRUD pedido por la evaluación. No se borran físicamente los registros: la tabla `auditoria` conserva la acción, el usuario y los datos anteriores. El autor original se mantiene al corregir un registro.

## Base de datos

H2 guarda sus archivos en `data/enfermeria.mv.db`, fuera del control de versiones. Reiniciar la aplicación conserva la información. No se necesita instalar un servidor de base de datos.

Tablas: `paciente`, `profesional`, `asignacion`, `signo_vital`, `valoracion`, `procedimiento`, `nota_enfermeria`, `auditoria`, `usuario`, `rol`, `usuario_rol` y `rol_permiso`. Cada registro clínico y asignación utiliza relaciones ManyToOne con paciente y profesional. No se propagan eliminaciones al historial.

### MySQL opcional

Crear la base con `database/mysql.sql`, configurar un usuario con permisos sobre esa base y ejecutar:

```powershell
$env:DB_URL='jdbc:mysql://localhost:3306/clinica_enfermeria?serverTimezone=America/Lima'
$env:DB_USER='tu_usuario'
$env:DB_PASSWORD='tu_contraseña'
.\mvnw.cmd spring-boot:run '-Dspring-boot.run.profiles=mysql'
```

No subas contraseñas ni archivos de datos a GitHub. El perfil MySQL está preparado, pero necesita una instancia MySQL para comprobar su conexión.

## API y Postman

Importa `postman/Enfermeria.postman_collection.json` para enfermería y `postman/Evaluacion02.postman_collection.json` para administración. Las colecciones usan HTTP Basic y variables para usuario, contraseña, paciente y registro. Ejecuta cada carpeta en orden: crear → historial → detalle → corregir → anular. La colección de evaluación 2 usa el usuario administrador y también captura IDs y versiones de los registros creados.

| Función | URL base |
|---|---|
| Asignados (GET) | /api/enfermeria/pacientes-asignados |
| Signos vitales | /api/pacientes/{pacienteId}/signos-vitales |
| Valoraciones | /api/pacientes/{pacienteId}/valoraciones |
| Procedimientos | /api/pacientes/{pacienteId}/procedimientos |
| Notas | /api/pacientes/{pacienteId}/notas |

Las cuatro URLs de registro admiten POST y GET. Añadiendo `/{id}` admiten GET, PUT y DELETE. DELETE recibe `{"motivo":"texto"}` y anula el registro. PUT requiere `version` y `motivoCambio`, además de los campos del registro. GET admite `desde`, `hasta`, `page` y `size`; procedimientos también `tipo`. El intervalo incluye el día final.

La API utiliza exclusivamente HTTP Basic sin sesiones; la interfaz utiliza sesión con protección CSRF. El servidor comprueba permisos: las enfermeras acceden a pacientes con asignaciones propias existentes; el supervisor y el administrador pueden acceder a todos. El médico puede consultar historiales pero no escribir cuidados; recepción no accede a datos clínicos. Es un criterio académico de acceso histórico y debe adaptarse a políticas institucionales antes de usar datos reales. Esta aplicación está configurada para escucharse solo localmente.

APIs adicionales: `/api/admin/usuarios`, `/api/admin/roles` (GET/POST), sus rutas `/{id}` (GET/PUT/DELETE) y `/{id}/estado` (PATCH), `/api/auditoria` (GET) y `/api/pacientes` (GET de datos personales). DELETE administrativo recibe `version` por query y desactiva; PATCH recibe `activo` y `version` en JSON.

## Arquitectura y entrega

Controladores MVC/REST → servicios con autorización, validación y transacciones → repositorios Spring Data JPA → base de datos. Dependencias inyectadas por constructor. Entidades y DTO separados; la API no expone hashes de contraseña. Pruebas en `src/test/java` y documentación funcional en `Requerimientos-enfermeria.md`. El flujo de GitHub Actions compila y ejecuta las pruebas con Java 17 cuando subas el proyecto.

Construir el ejecutable:

```powershell
.\mvnw.cmd clean package
java -jar target/enfermeria-1.0.0.jar
```

En Linux/macOS con Maven: `mvn test` y `mvn spring-boot:run`.

## Subir a GitHub

Crea un repositorio vacío en tu cuenta y, desde esta carpeta:

```powershell
git init
git add .
git commit -m "Implementar modulo de enfermeria"
git branch -M main
git remote add origin https://github.com/TU_USUARIO/TU_REPOSITORIO.git
git push -u origin main
```

El proyecto no crea ni publica automáticamente un repositorio. `.gitignore` excluye compilación, caché de herramientas, variables privadas y archivos de base de datos. Antes de entregar, genera tus capturas de la interfaz y las respuestas reales de Postman. No se incluyen capturas de resultados inventados.
