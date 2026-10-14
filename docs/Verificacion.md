# Verificación del proyecto

Verificación local realizada el 14/10/2026 con Java 25, Maven y Spring Boot 3.5.6.

## Pruebas automatizadas

Comando: `mvnw.cmd test`.

Resultado: **11 pruebas, 0 fallos, 0 errores**. El reporte se genera en `target/surefire-reports/pe.clinica.enfermeria.EnfermeriaIntegrationTest.txt`.

Se comprobaron:

- Registro, detalle, historial, corrección, control de versión y anulación para signos, valoraciones, procedimientos y notas.
- Conservación de datos anteriores en auditoría.
- Separación de registros entre pacientes y rechazo del acceso de una enfermera sin asignación.
- Filtros de fechas, inclusión del día final y límites de paginación.
- Validaciones de dolor, saturación, presión incompleta, medición vacía, notas vacías y procedimientos “Otros”.
- Respuestas ante JSON inválido y credenciales incorrectas.
- Consulta de asignaciones propias y consulta de supervisión.
- Renderizado de formularios e historiales, protección CSRF y escape del contenido de notas.

## Comprobación con la aplicación ejecutándose

Se compiló `target/enfermeria-1.0.0.jar` y se ejecutó con la base H2 persistente predeterminada.

- Inicio de sesión real mediante formulario, cookies y token CSRF: correcto.
- Panel, ficha, cuatro historiales y cuatro formularios: HTTP 200, diez páginas en total.
- API de asignaciones: HTTP 200, dos pacientes para Ana.
- Creación de nota mediante API: HTTP 201.
- Se detuvo la JVM y se inició una nueva. La nota continuó disponible desde el archivo H2.

La base de demostración local contiene una nota identificada como comprobación de persistencia. El archivo de datos se excluye de GitHub.

No se ejecutó una revisión visual en navegador automatizado, ni la conexión a una instancia MySQL. Las páginas se verificaron mediante renderizado Thymeleaf y solicitudes HTTP reales. El flujo de GitHub Actions se incluye para ejecutar la compilación y pruebas en GitHub; todavía no se ha ejecutado allí.
