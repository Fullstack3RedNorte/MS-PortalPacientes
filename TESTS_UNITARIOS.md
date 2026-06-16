# Pruebas unitarias — MS Portal Pacientes

Este documento enumera y documenta las pruebas unitarias añadidas, con su propósito y mapeo a los criterios de aceptación del servicio.

Resumen: 12 pruebas unitarias divididas en dos grupos (service y controller).

1) Service: `PortalPacientesServiceImplTest` (7 pruebas)
  - Archivo: src/test/java/cl/rednorte/ms_portal_pacientes/service/impl/PortalPacientesServiceImplTest.java
  1.1 `obtenerSolicitudes_success`
    - Propósito: Verifica que `obtenerSolicitudes` retorna correctamente una `PageResponse` cuando el MS Lista de Espera responde con datos.
    - Criterio mapeado: GET /portal/solicitudes → 200 y contenido paginado.

  1.2 `obtenerSolicitudes_serviceUnavailableOnException`
    - Propósito: Simula una excepción del cliente HTTP y valida que se lance `ResponseStatusException` con 503.
    - Criterio mapeado: Si MS Lista de Espera no está disponible → 503 Servicio no disponible.

  1.3 `obtenerDetalle_success`
    - Propósito: Verifica que `obtenerDetalle` retorna un `SolicitudDetalleResponse` cuando el MS Lista de Espera devuelve un detalle válido.
    - Criterio mapeado: GET /portal/solicitudes/{id}?rutPaciente=... → 200 con detalle e historial.

  1.4 `obtenerDetalle_notFoundWhenNull`
    - Propósito: Simula que el servicio remoto devuelve `null` y valida que se lance `ResponseStatusException` con 404.
    - Criterio mapeado: Solicitud no encontrada → 404.

  1.5 `obtenerDetalle_serviceUnavailableOnException`
    - Propósito: Simula error del cliente HTTP al pedir detalle y valida 503.
    - Criterio mapeado: MS Lista de Espera no disponible → 503.

  1.6 `obtenerSolicitudes_nullBody_returnsNull`
    - Propósito: Comprueba comportamiento cuando la llamada HTTP retorna 200 pero con body null.
    - Criterio mapeado: Robustez frente a respuestas inesperadas del MS Lista de Espera.

  1.7 `obtenerSolicitudes_buildsUrlWithRutAndPagination`
    - Propósito: Verifica que la URL solicitada al MS Lista de Espera contiene `rutPaciente`, `page` y `size`.
    - Criterio mapeado: Correcta composición de la llamada HTTP externa hacia MS Lista de Espera.

2) Controller: `PortalPacientesControllerTest` (5 pruebas)
  - Archivo: src/test/java/cl/rednorte/ms_portal_pacientes/controller/PortalPacientesControllerTest.java
  2.1 `obtenerSolicitudes_returnsOkAndContent`
    - Propósito: Prueba de integración del controlador (MockMvc) que valida respuesta 200 y el JSON contiene la solicitud esperada.
    - Criterio mapeado: Endpoint GET /portal/solicitudes devolviendo lista paginada visible para el paciente.

  2.2 `obtenerDetalle_returnsOk`
    - Propósito: Valida que GET /solicitudes/{id}?rutPaciente=. devuelve 200 y el JSON del detalle contiene el id solicitado.
    - Criterio mapeado: Endpoint de detalle devuelve la estructura esperada con `historial`.

  2.3 `obtenerDetalle_returnsNotFoundWhenServiceThrows404`
  2.4 `obtenerSolicitudes_missingRutPaciente_returnsBadRequest`
    - Propósito: Verifica que si falta el parámetro obligatorio `rutPaciente`, el controlador responde 400.
    - Criterio mapeado: El endpoint requiere `rutPaciente` (entrada sin autenticación) — parámetros obligatorios deben validarse.

  2.5 `obtenerSolicitudes_forwardsPaginationParametersToService`
    - Propósito: Valida que `page` y `size` se pasen correctamente desde la petición al servicio.
    - Criterio mapeado: Soporte de paginación en GET /portal/solicitudes (parámetros `page` y `size`).

Las nuevas pruebas están implementadas en los mismos archivos de test previamente indicados.

Cómo ejecutar las pruebas

```bash
./mvnw.cmd test
```

Notas adicionales
- Las pruebas de servicio usan `Mockito` para mockear `RestTemplate`.
- Las pruebas de controlador usan `MockMvc` con el servicio mockeado.
- Los nombres de los tests siguen el patrón `givenWhenThen` / `verb_expectedOutcome` para facilitar trazabilidad.

Mapeo detallado a criterios de aceptación
-------------------------------------

Las pruebas anteriores se vinculan directamente con los criterios de aceptación descritos en la documentación técnica del proyecto: [ms-portal-pacientes-documentacion.md](ms-portal-pacientes-documentacion.md).

- Criterio: "GET /portal/solicitudes → Response 200 con lista paginada".
  - Tests que lo cubren: `obtenerSolicitudes_success` (service) y `obtenerSolicitudes_returnsOkAndContent` (controller).

- Criterio: "Si MS Lista de Espera no está disponible → Response 503".
  - Tests que lo cubren: `obtenerSolicitudes_serviceUnavailableOnException` (service) y `obtenerDetalle_serviceUnavailableOnException` (service).

- Criterio: "GET /portal/solicitudes/{id}?rutPaciente=... → Response 200 con detalle y `historial`".
  - Tests que lo cubren: `obtenerDetalle_success` (service) y `obtenerDetalle_returnsOk` (controller).

- Criterio: "Solicitud no encontrada → Response 404".
  - Tests que lo cubren: `obtenerDetalle_notFoundWhenNull` (service) y `obtenerDetalle_returnsNotFoundWhenServiceThrows404` (controller).

Trazabilidad y recomendaciones
------------------------------

- Cada prueba incluye ahora comentarios Javadoc que explican el escenario Given/When/Then para facilitar la revisión.
- Para cubrir criterios adicionales (por ejemplo: validación de `rutPaciente` faltante o parámetros de paginación fuera de rango) se pueden añadir tests complementarios en `PortalPacientesControllerTest`.

