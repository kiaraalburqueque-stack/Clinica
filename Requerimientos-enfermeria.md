# Desarrollo de los ocho requerimientos de enfermería

Implementación disponible en este proyecto: consulta `README.md` para ejecutarla y `docs/Verificacion.md` para revisar las comprobaciones realizadas. Las secciones de diseño que siguen describen la especificación original.

Fuente: Evaluacion 01-1 (1).docx, módulo 8 — Enfermería. Este documento desarrolla los ocho requisitos seleccionados por el equipo. Las reglas de validación y los criterios de aceptación son propuestas de implementación; los campos se basan en las secciones del documento fuente.

## Alcance y distribución

| Persona | Registro o consulta | Historial o consulta complementaria |
|---|---|---|
| 1 | RF-ENF-10: Registrar signos vitales | RF-ENF-12: Historial de signos vitales |
| 2 | RF-ENF-07: Registrar valoración | RF-ENF-09: Historial de valoraciones |
| 3 | RF-ENF-25: Registrar procedimientos | RF-ENF-27: Historial de procedimientos |
| 4 | RF-ENF-31: Registrar notas de enfermería | RF-ENF-06: Consultar pacientes asignados |

Actor principal: profesional de enfermería autenticado. Dependencias mínimas: pacientes, profesionales y asignaciones existentes. La administración de estos catálogos corresponde a otros requisitos; para una demostración pueden cargarse datos ficticios.

## Persona 1

### RF-ENF-10 — Registrar signos vitales

Objetivo: guardar las mediciones realizadas a un paciente para su seguimiento.

Precondiciones: sesión activa, paciente existente y acceso autorizado al paciente.

Datos: paciente, fecha y hora de medición, profesional responsable, temperatura (°C), presión sistólica y diastólica (mmHg), frecuencia cardíaca (latidos/min), frecuencia respiratoria (respiraciones/min), saturación de oxígeno (%), peso (kg), talla (cm), dolor (0–10) y glucosa cuando corresponda. La unidad de glucosa debe quedar explícita; se propone mg/dL para el prototipo.

Flujo: seleccionar paciente, abrir formulario, ingresar mediciones, guardar, validar y mostrar confirmación con el registro creado.

Reglas propuestas: identificar al profesional desde la sesión; requerir fecha/hora y al menos una medición; aceptar mediciones parciales; si se ingresa presión arterial, exigir ambos componentes; rechazar números negativos, saturación fuera de 0–100 y dolor fuera de 0–10. No rechazar automáticamente valores clínicamente anormales: pueden representar mediciones reales. No se implementan alertas clínicas como parte de este alcance.

Resultado: registro persistido vinculado al paciente y al profesional.

Criterios de aceptación:

- Un envío válido crea un registro y muestra confirmación.
- Los valores y unidades se conservan al consultar el registro.
- Los datos inválidos muestran errores y no se guardan.
- No se puede registrar información de un paciente sin autorización.

### RF-ENF-12 — Historial de signos vitales

Objetivo: consultar las mediciones anteriores del paciente sin sobrescribirlas.

Datos de salida: fecha/hora, mediciones con sus unidades y profesional responsable.

Flujo: seleccionar paciente, abrir historial, consultar registros y filtrar por intervalo de fechas. Se propone orden descendente por fecha/hora y paginación.

Reglas propuestas: mostrar únicamente registros del paciente elegido; incluir todo el día final cuando el filtro se expresa en fechas; validar que la fecha inicial no sea posterior a la final; representar mediciones ausentes como “No registrado”.

Criterios de aceptación:

- Dos registros del mismo paciente aparecen como entradas independientes.
- Los registros de otro paciente no aparecen en el historial.
- Los filtros muestran únicamente las entradas del intervalo indicado.
- Un paciente sin mediciones muestra un estado vacío comprensible.

## Persona 2

### RF-ENF-07 — Registrar valoración

Objetivo: documentar la valoración de enfermería realizada al paciente.

Precondiciones: profesional autenticado y acceso autorizado al paciente.

Datos: paciente, fecha/hora, profesional, estado general, estado de conciencia, dolor, movilidad, alimentación, higiene, estado de piel, riesgo de caída, riesgo de lesiones por presión, necesidades de cuidado y observaciones.

Flujo: seleccionar paciente, completar valoración, guardar y recibir confirmación.

Reglas propuestas: requerir estado general, estado de conciencia y necesidades de cuidado; los demás campos pueden quedar sin registrar; dolor entre 0 y 10 si se informa; usar categorías configuradas para los riesgos y conservar “No evaluado” como opción. No calcular puntuaciones de escalas clínicas que el documento no especifica.

Resultado: nueva valoración vinculada al paciente y al profesional.

Criterios de aceptación:

- Una valoración válida se guarda con paciente, fecha/hora y autor.
- Los campos obligatorios vacíos muestran errores.
- La valoración puede consultarse después de guardarse.
- Una nueva valoración no reemplaza las anteriores.

### RF-ENF-09 — Historial de valoraciones

Objetivo: conservar y consultar las valoraciones realizadas al paciente.

Datos de salida: fecha/hora, profesional, resumen de estado general y acceso al detalle completo.

Flujo: seleccionar paciente, consultar historial, filtrar por fechas y abrir una valoración.

Reglas propuestas: ordenar de más reciente a más antigua; conservar cada valoración como registro independiente; restringir el detalle con las mismas reglas de acceso que el listado.

Criterios de aceptación:

- Se visualizan todas las valoraciones autorizadas del paciente.
- El detalle muestra todos los datos registrados.
- Los filtros y el estado vacío funcionan.
- No se accede a valoraciones de pacientes no autorizados mediante URL directa.

## Persona 3

### RF-ENF-25 — Registrar procedimientos

Objetivo: registrar los procedimientos de enfermería ejecutados.

Datos: paciente, procedimiento, fecha/hora, profesional, resultado y observaciones.

Tipos contemplados en la fuente: curaciones, cambios de posición, higiene, control de glucosa, administración de oxígeno, colocación de dispositivos, retiro de dispositivos y otros cuidados. “Otros” debe permitir describir el procedimiento.

Flujo: seleccionar paciente, elegir procedimiento, registrar resultado y observaciones, guardar y recibir confirmación.

Reglas propuestas: requerir procedimiento y resultado; exigir descripción cuando se elige “Otros”; atribuir el registro al profesional autenticado. Esta función documenta la ejecución, no prescribe ni programa procedimientos.

Criterios de aceptación:

- Un procedimiento válido se guarda con paciente, fecha/hora y responsable.
- Un procedimiento sin tipo o resultado no se guarda.
- El procedimiento aparece en el historial del paciente.
- No se permite registrar procedimientos de pacientes sin autorización.

### RF-ENF-27 — Historial de procedimientos

Objetivo: mantener la secuencia de procedimientos realizados al paciente.

Datos de salida: procedimiento, fecha/hora, profesional, resultado y observaciones.

Flujo: seleccionar paciente, abrir historial y filtrar por fechas o tipo de procedimiento.

Reglas propuestas: conservar registros independientes, ordenar por fecha/hora descendente y permitir consultar el detalle de observaciones.

Criterios de aceptación:

- Los procedimientos registrados se muestran con todos sus datos.
- El historial no mezcla pacientes.
- Los filtros por fecha y procedimiento funcionan de forma conjunta.
- Un paciente sin procedimientos muestra un estado vacío.

## Persona 4

### RF-ENF-31 — Registrar notas de enfermería

Objetivo: documentar las actividades y observaciones del profesional durante el turno.

Datos: paciente, fecha/hora, profesional y contenido de la nota. El turno puede asociarse si existe una asignación vigente.

Flujo: seleccionar paciente, escribir nota, guardar y mostrar confirmación con el contenido registrado.

Reglas propuestas: requerir contenido con texto distinto de espacios; conservar saltos de línea; atribuir el autor desde la sesión; almacenar la nota vinculada al paciente; presentar el texto con escape HTML para evitar ejecución de contenido introducido por el usuario.

Criterios de aceptación:

- Una nota válida se guarda con su autor y fecha/hora.
- Una nota vacía no se guarda y muestra un error.
- El contenido guardado puede consultarse en la ficha del paciente.
- Varias notas se conservan como entradas independientes.

### RF-ENF-06 — Consultar pacientes asignados

Objetivo: consultar los pacientes asignados a cada profesional de enfermería.

Precondiciones: asignaciones registradas por la funcionalidad correspondiente y permisos para consultar las del profesional solicitado.

Datos de salida: paciente, código o documento, profesional asignado, fecha, turno, servicio, habitación y cama cuando corresponda.

Flujo: ingresar a “Pacientes asignados”, consultar las asignaciones propias, filtrar por fecha/turno/servicio y abrir la ficha de un paciente.

Reglas propuestas: por defecto mostrar las asignaciones propias del día en America/Lima; permitir consultar otros días autorizados; solo un rol con permiso de supervisión podrá consultar asignaciones de otro profesional; validar el permiso en el servidor. La creación y modificación de asignaciones quedan fuera de estos ocho requisitos.

Criterios de aceptación:

- El profesional ve únicamente las asignaciones que está autorizado a consultar.
- Cada paciente aparece con turno, servicio y ubicación disponibles.
- Los filtros funcionan y no exponen asignaciones ajenas.
- Sin asignaciones se muestra un mensaje claro.
- Cambiar el identificador del profesional en la URL no permite consultar asignaciones sin permiso.

## Diseño para cumplir la evaluación

La evaluación solicita Spring Boot, IoC e inyección de dependencias, APIs REST con CRUD, persistencia JPA/Hibernate, relaciones entre entidades, vistas Thymeleaf y evidencias de pruebas. Esta sección define el diseño; no constituye código implementado ni pruebas ejecutadas.

Arquitectura propuesta: controladores REST y MVC → servicios con reglas y autorización → repositorios Spring Data JPA → base de datos. Usar inyección por constructor y DTO con validación para evitar que el cliente modifique libremente entidades persistentes.

Entidades mínimas:

| Entidad | Relaciones y datos principales |
|---|---|
| Paciente | ID, código, documento, nombres; OneToMany con registros clínicos y asignaciones |
| Profesional | ID, usuario y nombre; OneToMany con registros y asignaciones |
| Asignacion | ManyToOne con paciente y profesional; fecha, turno, servicio, habitación y cama |
| SignoVital | ManyToOne con paciente y profesional; fecha/hora y mediciones |
| Valoracion | ManyToOne con paciente y profesional; fecha/hora y campos de valoración |
| Procedimiento | ManyToOne con paciente y profesional; fecha/hora, tipo, resultado y observaciones |
| NotaEnfermeria | ManyToOne con paciente y profesional; fecha/hora y contenido |

Guardar las relaciones como claves foráneas. No configurar borrado en cascada desde paciente o profesional hacia el historial. Se propone conservar los registros y anularlos con motivo si el trabajo exige una operación de eliminación; esta política es una decisión de diseño, no un requisito explícito de los ocho seleccionados.

## APIs propuestas

| Requisito | Método | Endpoint | Resultado esperado |
|---|---|---|---|
| RF-ENF-10 | POST | /api/pacientes/{pacienteId}/signos-vitales | 201 con registro creado |
| RF-ENF-12 | GET | /api/pacientes/{pacienteId}/signos-vitales | 200 con historial paginado |
| RF-ENF-07 | POST | /api/pacientes/{pacienteId}/valoraciones | 201 con valoración creada |
| RF-ENF-09 | GET | /api/pacientes/{pacienteId}/valoraciones | 200 con historial paginado |
| RF-ENF-25 | POST | /api/pacientes/{pacienteId}/procedimientos | 201 con procedimiento creado |
| RF-ENF-27 | GET | /api/pacientes/{pacienteId}/procedimientos | 200 con historial paginado |
| RF-ENF-31 | POST | /api/pacientes/{pacienteId}/notas | 201 con nota creada |
| RF-ENF-06 | GET | /api/enfermeria/pacientes-asignados | 200 con asignaciones autorizadas |

Los historiales aceptan desde, hasta, page y size; procedimientos también acepta tipo. Consultar una entrada individual requiere GET con su ID y validación del acceso al paciente.

Errores propuestos: 400 para datos inválidos, 401 sin autenticación, 403 sin permiso y 404 para recursos inexistentes. Ningún POST recibe el autor como un dato libre del cliente.

Los ocho requisitos seleccionados no solicitan expresamente editar ni eliminar registros. Para cumplir el criterio general de CRUD de la evaluación se deberán definir adicionalmente actualización y anulación, con autorización y conservación de trazabilidad. No debe presentarse registro + historial como CRUD completo.

## Vistas Thymeleaf propuestas

- Pacientes asignados: tabla, filtros y acceso a ficha.
- Ficha del paciente: identificación y navegación hacia las cuatro funciones.
- Signos vitales: formulario y tabla de historial con unidades.
- Valoraciones: formulario, historial y detalle.
- Procedimientos: formulario e historial con filtros.
- Notas de enfermería: formulario y consulta de las notas guardadas.

Todos los formularios deben mostrar errores junto al campo, confirmar la operación y conservar los datos introducidos cuando la validación falla. Las operaciones de escritura deben protegerse contra CSRF cuando se utiliza autenticación por sesión.

## Plan de pruebas y evidencia

| Responsable | Pruebas principales |
|---|---|
| Persona 1 | Crear signos válidos; rechazar dolor inválido; guardar dos mediciones; consultar historial y filtros; comprobar separación entre pacientes |
| Persona 2 | Crear valoración; rechazar campos obligatorios vacíos; conservar dos valoraciones; consultar detalle y fechas |
| Persona 3 | Crear procedimiento; validar “Otros”; rechazar ausencia de resultado; consultar historial y filtros combinados |
| Persona 4 | Crear nota; rechazar nota vacía; conservar saltos de línea; listar asignados; comprobar filtros y acceso a asignaciones ajenas |

Pruebas comunes: sesión ausente, paciente inexistente, acceso no autorizado, persistencia tras reiniciar y autorización al acceder por ID directamente. Usar datos ficticios.

Evidencia que debe generarse al implementar: solicitudes y respuestas reales de Postman, capturas de las vistas operativas, registros persistidos y resultados de pruebas. Este documento contiene un plan; no presenta evidencia de ejecución.
