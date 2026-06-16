# Pruebas unitarias — MS Portal Pacientes

**RedNorte | Fullstack III | DuocUC**

---

## Índice

1. [Descripción general](#1-descripci%C3%B3n-general)
2. [Alcance](#2-alcance)
3. [Catálogo de pruebas](#3-cat%C3%A1logo-de-pruebas)
4. [Trazabilidad a criterios de aceptación](#4-trazabilidad-a-criterios-de-aceptaci%C3%B3n)
5. [Ejecución](#5-ejecuci%C3%B3n)
6. [Notas adicionales](#6-notas-adicionales)

---

## 1. Descripción general

Este documento presenta las pruebas unitarias del microservicio **MS Portal Pacientes**, organizadas de forma similar a la documentación técnica del proyecto. Su objetivo es dejar trazabilidad entre cada prueba, la clase que la contiene y los criterios de aceptación definidos en el documento base entregado por el usuario.

La cobertura actual considera:

- 1 prueba de arranque del contexto Spring.
- 7 pruebas para `PortalPacientesServiceImpl`.
- 5 pruebas para `PortalPacientesController`.

**Total ejecutado por Maven: 13 pruebas.**

---

## 2. Alcance

### Estructura de pruebas

| Tipo | Clase | Cantidad | Propósito |
|------|-------|----------|-----------|
| Arranque | `MsPortalPacientesApplicationTests` | 1 | Verificar que el contexto Spring carga correctamente. |
| Service | `PortalPacientesServiceImplTest` | 7 | Validar lógica de consumo HTTP y manejo de errores. |
| Controller | `PortalPacientesControllerTest` | 5 | Validar respuestas HTTP y delegación al servicio. |

### Herramientas de prueba

| Herramienta | Uso |
|------------|-----|
| JUnit 5 | Ejecución de pruebas unitarias |
| Mockito | Simulación de dependencias externas |
| MockMvc | Pruebas del controlador sin levantar servidor |
| Spring Boot Test | Verificación de contexto de aplicación |

---

## 3. Catálogo de pruebas

### 3.1 `MsPortalPacientesApplicationTests`

| Prueba | Propósito | Resultado esperado |
|--------|-----------|--------------------|
| `contextLoads` | Verifica que el contexto de Spring Boot inicia correctamente. | La aplicación arranca sin errores. |

### 3.2 `PortalPacientesServiceImplTest`

| Prueba | Propósito | Criterio asociado |
|--------|-----------|-------------------|
| `obtenerSolicitudes_success` | Retorna una `PageResponse` válida cuando el MS Lista de Espera responde con datos. | GET `/portal/solicitudes` → 200 con contenido paginado. |
| `obtenerSolicitudes_serviceUnavailableOnException` | Valida que un error del cliente HTTP se traduzca en 503. | MS Lista de Espera no disponible → 503. |
| `obtenerDetalle_success` | Retorna el detalle de una solicitud cuando el servicio remoto responde correctamente. | GET `/portal/solicitudes/{id}` → 200 con detalle e historial. |
| `obtenerDetalle_notFoundWhenNull` | Valida que una respuesta `null` se traduzca en 404. | Solicitud no encontrada → 404. |
| `obtenerDetalle_serviceUnavailableOnException` | Valida que un fallo del cliente HTTP en el detalle se traduzca en 503. | MS Lista de Espera no disponible → 503. |
| `obtenerSolicitudes_nullBody_returnsNull` | Comprueba el comportamiento cuando la llamada responde 200 con body nulo. | Robustez ante respuesta inesperada. |
| `obtenerSolicitudes_buildsUrlWithRutAndPagination` | Verifica que la URL construida incluya `rutPaciente`, `page` y `size`. | Composición correcta de la llamada HTTP. |

### 3.3 `PortalPacientesControllerTest`

| Prueba | Propósito | Criterio asociado |
|--------|-----------|-------------------|
| `obtenerSolicitudes_returnsOkAndContent` | Valida la respuesta 200 y el JSON de una lista paginada. | GET `/portal/solicitudes` → 200 con lista paginada. |
| `obtenerSolicitudes_missingRutPaciente_returnsBadRequest` | Valida que falte `rutPaciente` y responda 400. | El parámetro `rutPaciente` es obligatorio. |
| `obtenerSolicitudes_forwardsPaginationParametersToService` | Verifica que `page` y `size` lleguen al servicio. | Soporte de paginación en GET `/portal/solicitudes`. |
| `obtenerDetalle_returnsOk` | Valida la respuesta 200 y el JSON del detalle. | GET `/portal/solicitudes/{id}` → 200 con detalle e historial. |
| `obtenerDetalle_returnsNotFoundWhenServiceThrows404` | Valida la propagación de 404 cuando el servicio lo devuelve. | Solicitud no encontrada → 404. |

---

## 4. Trazabilidad a criterios de aceptación

Los criterios de aceptación se tomaron como base del documento DOCX entregado por el usuario.

### 4.1 Criterio: GET `/portal/solicitudes` → 200 con lista paginada

| Prueba | Capa |
|--------|------|
| `obtenerSolicitudes_success` | Service |
| `obtenerSolicitudes_returnsOkAndContent` | Controller |

### 4.2 Criterio: GET `/portal/solicitudes/{id}?rutPaciente=...` → 200 con detalle e historial

| Prueba | Capa |
|--------|------|
| `obtenerDetalle_success` | Service |
| `obtenerDetalle_returnsOk` | Controller |

### 4.3 Criterio: solicitud no encontrada → 404

| Prueba | Capa |
|--------|------|
| `obtenerDetalle_notFoundWhenNull` | Service |
| `obtenerDetalle_returnsNotFoundWhenServiceThrows404` | Controller |

### 4.4 Criterio: MS Lista de Espera no disponible → 503

| Prueba | Capa |
|--------|------|
| `obtenerSolicitudes_serviceUnavailableOnException` | Service |
| `obtenerDetalle_serviceUnavailableOnException` | Service |

### 4.5 Criterio: validación de parámetros obligatorios y paginación

| Prueba | Capa |
|--------|------|
| `obtenerSolicitudes_missingRutPaciente_returnsBadRequest` | Controller |
| `obtenerSolicitudes_forwardsPaginationParametersToService` | Controller |
| `obtenerSolicitudes_buildsUrlWithRutAndPagination` | Service |

---

## 5. Ejecución

```bash
./mvnw.cmd test
```

---

## 6. Notas adicionales

- Las pruebas de servicio usan `Mockito` para mockear `RestTemplate`.
- Las pruebas de controlador usan `MockMvc` con el servicio mockeado.
- Los nombres de los tests siguen el patrón Given/When/Then o `verb_expectedOutcome` para facilitar trazabilidad.
- Cada prueba está comentada con el escenario esperado para facilitar revisión y mantención.

---

*Documentación de pruebas generada para el proyecto semestral Fullstack III — RedNorte — DuocUC 2026*

