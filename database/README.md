# Modelo de base de datos

El perfil predeterminado usa H2 en archivo: `data/enfermeria.mv.db`. Hibernate genera las tablas a partir de las entidades; el archivo se crea al iniciar la aplicación y conserva los registros entre reinicios.

```mermaid
erDiagram
    PACIENTE ||--o{ ASIGNACION : recibe
    PROFESIONAL ||--o{ ASIGNACION : atiende
    PACIENTE ||--o{ SIGNO_VITAL : tiene
    PROFESIONAL ||--o{ SIGNO_VITAL : registra
    PACIENTE ||--o{ VALORACION : tiene
    PROFESIONAL ||--o{ VALORACION : registra
    PACIENTE ||--o{ PROCEDIMIENTO : tiene
    PROFESIONAL ||--o{ PROCEDIMIENTO : registra
    PACIENTE ||--o{ NOTA_ENFERMERIA : tiene
    PROFESIONAL ||--o{ NOTA_ENFERMERIA : registra
```

`auditoria` conserva tipo de registro, ID, acción, usuario, fecha/hora, motivo y datos anteriores a una corrección o anulación. Cada registro clínico incluye `version` para concurrencia, `anulado` y `motivo_anulacion`. La anulación conserva el registro y sus relaciones.

Las claves foráneas `paciente_id` y `profesional_id` son obligatorias. El documento y código de paciente son únicos, al igual que el usuario del profesional. Las contraseñas de profesionales se guardan como hash BCrypt.

Para MySQL, crea la base con `mysql.sql` y usa las variables y el comando documentados en el README principal. La creación automática de tablas es apropiada para esta demostración; un despliegue institucional necesitaría migraciones versionadas y sus propias políticas de acceso y respaldo.
