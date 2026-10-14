# Revisión de estabilidad

Revisión local del 14/10/2026. Resultado: 25 pruebas ejecutadas, 0 fallos y 0 errores.

Cambios comprobados:

- Las listas de roles y permisos rechazan elementos nulos con HTTP 400, sin crear registros ni auditorías de éxito.
- Los errores de los controladores web se muestran como páginas HTML y los errores REST como JSON. Se corrigió el alcance y la prioridad de los manejadores globales.
- La interfaz presenta el motivo específico del error cuando está disponible.
- Los parámetros obligatorios ausentes y fechas mal formadas producen mensajes claros de validación.
- La validación de fechas utiliza America/Lima, igual que los formularios y registros.

Las pruebas incluyen los cuatro flujos de formulario completos: crear → abrir corrección → corregir con motivo → anular → comprobar que el historial conserva el registro. Se mantienen las pruebas de permisos, sesiones desactivadas, auditoría, migración y relaciones.

Para repetir la comprobación: `mvnw.cmd test`. Los reportes se generan en `target/surefire-reports/`.

Esta revisión verifica los flujos y casos cubiertos; no garantiza ausencia de cualquier fallo posible. La base comprobada es H2. La conexión MySQL y la inspección visual en un navegador automatizado siguen sin verificarse.

Si aparece un error de arranque indicando que la base H2 está en uso, detén la instancia anterior de esta aplicación con Ctrl+C en su terminal antes de iniciar otra. No borres el archivo de base de datos ni cierres indiscriminadamente todos los procesos Java.
