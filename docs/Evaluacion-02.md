# Evaluación 02: implementación y demostración

Las mejoras se aplican al proyecto de enfermería de la evaluación 1. Se conservan sus ocho requisitos y se agregan las funcionalidades solicitadas en `Evaluacion 02-2 (2).docx`. El documento utiliza gestión de pedidos en el encabezado, pero su procedimiento indica ampliar el proyecto existente.

## Correspondencia con las preguntas

| Pregunta | Implementación | Evidencia reproducible |
|---|---|---|
| 1. Relaciones Hibernate/JPA | Relaciones clínicas ManyToOne/OneToMany; Usuario–Profesional OneToOne con FK única; Usuario–Rol ManyToMany con tabla intermedia | Entidades, diagrama en `database/README.md`, formularios y CRUD relacionados |
| 2. Auditoría | `@Auditable` y `AuditAspect` registran creación, modificación, cambio de estado, desactivación y anulación | `/auditoria` y `/api/auditoria`, tabla `auditoria`, pruebas de AOP |
| 3. Usuarios y roles | Registro, listado, edición, activación/desactivación, asignación de uno o varios roles | `/api/admin/usuarios`, `/api/admin/roles` y servicios administrativos |
| 4. Frontend | Listados y formularios Thymeleaf de usuarios y roles, selección de roles y permisos, botones de estado | `/admin/usuarios` y `/admin/roles` |
| 5. Control de acceso | Permisos procedentes de roles activos, restricciones de rutas y servicios, opciones visibles y destinos por perfil | Spring Security, `@PreAuthorize`, filtro de estado y pruebas de perfiles |

Las relaciones por integrante se pueden explicar con estas parejas: Persona 1, SignoVital–Paciente; Persona 2, Valoracion–Paciente; Persona 3, Procedimiento–Paciente; Persona 4, NotaEnfermeria–Paciente. Todas tienen además una relación con Profesional.

## Perfiles iniciales

| Cuenta demo | Rol | Destino y acciones |
|---|---|---|
| admin | ADMINISTRADOR | Usuarios, roles, auditoría y funcionalidades existentes |
| medico | MEDICO | Pacientes, fichas e historiales; consulta clínica |
| recepcion | RECEPCIONISTA | Pacientes y datos personales |
| ana / elena | ENFERMERIA | Pacientes asignados y registros clínicos propios autorizados |
| supervisor | SUPERVISOR | Asignaciones de otros profesionales, registros y auditoría |

Contraseña demo de estas cuentas: `Demo123!`. Los códigos de roles no llevan tildes; los nombres visibles sí. Los permisos se asignan al rol, y un usuario puede tener varios roles. Para un rol que registre cuidados, seleccionar tanto el permiso de registro de enfermería como el de consulta clínica.

## Demostración desde la interfaz

1. Ejecutar `mvnw.cmd spring-boot:run` y acceder a http://localhost:8080.
2. Entrar como `admin`: la aplicación redirige a Usuarios.
3. Crear un rol, seleccionar sus permisos y guardarlo. Editar su nombre o descripción y cambiar su estado.
4. Crear un usuario con nombre, correo, contraseña y rol activo. Editarlo; dejar la contraseña vacía conserva la anterior. Desactivarlo y reactivarlo.
5. Abrir Auditoría: consultar las operaciones anteriores por usuario, entidad, acción y fechas. Expandir detalles para ver los datos anteriores de una modificación.
6. Entrar como `medico`: consultar pacientes e historiales; no aparecen botones para registrar, corregir o anular cuidados.
7. Entrar como `recepcion`: ver datos personales; no aparece la ficha clínica. Intentar una URL clínica directamente devuelve 403.
8. Entrar como `ana`: registrar un cuidado, corregirlo con motivo y anularlo. Consultar sus entradas de auditoría con `admin` o `supervisor`.
9. Con una cuenta de prueba abierta en otra sesión, desactivar su usuario o su único rol desde administración. La siguiente petición de esa sesión pierde acceso.

La base H2 se crea y actualiza automáticamente en `data/`. No hace falta modificar tablas directamente para administrar usuarios o roles.

## Auditoría, validación e integridad

El aspecto se ejecuta después de comprobar la autorización y dentro de la transacción de negocio. Un fallo en la operación o en la auditoría revierte los cambios. La auditoría guarda usuario, fecha/hora de Lima, acción, entidad, ID, motivo cuando corresponde y la instantánea previa a los cambios. Las instantáneas administrativas excluyen contraseñas y hashes.

Las escrituras exigen versiones vigentes, datos válidos y relaciones existentes. Solo se asignan roles activos. Los nombres de usuario, correos y códigos de rol son únicos. Las contraseñas se guardan con BCrypt. No se elimina físicamente a los usuarios, roles ni registros clínicos. La cuenta y las relaciones de Profesional originales se conservan para no alterar el historial existente.

## Pruebas y límites de evidencia

Las 11 pruebas de enfermería y 8 pruebas nuevas comprueban CRUD administrativo, roles, relaciones, AOP, ausencia de contraseñas en respuestas y auditoría, revocación de sesiones, redirecciones, formularios con CSRF, duplicados, protección del último administrador y migración idempotente. Una prueba específica crea una cuenta antigua con una nota clínica y comprueba que la migración conserva el hash, el profesional y el registro.

Comando: `mvnw.cmd test`. Los reportes de ejecución se generan en `target/surefire-reports/`. La colección `postman/Evaluacion02.postman_collection.json` permite repetir las operaciones administrativas.

Las capturas para entregar deben tomarse de la aplicación funcionando. No se incluyen capturas fabricadas. El perfil MySQL continúa disponible y requiere una instancia propia para verificar su conexión; la persistencia y actualización de datos se verifican localmente con H2.

Verificación HTTP real: siete páginas administrativas respondieron correctamente y se comprobaron las redirecciones de administrador/médico/recepción, la consulta médica y el bloqueo de recepción sobre información clínica. Se utilizó una base H2 aislada porque la base original estaba abierta en otra instancia. Para cargar la versión nueva y sus tablas en tu base original, detén la aplicación anterior y vuelve a ejecutar `mvnw.cmd spring-boot:run`. No se detuvo la instancia del usuario ni se modificó su archivo de datos durante estas comprobaciones.

Referencias de implementación: [autorización de métodos de Spring Security](https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html) y [orden de los interceptores transaccionales](https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/annotations.html).
