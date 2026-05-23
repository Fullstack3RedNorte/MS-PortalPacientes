# MS Portal Pacientes — Documentación Técnica
**RedNorte | Fullstack III | DuocUC**

---

## Índice

1. [Descripción general](#1-descripción-general)
2. [Arquetipo del microservicio](#2-arquetipo-del-microservicio)
3. [Arquitectura](#3-arquitectura)
4. [Patrones de diseño de software](#4-patrones-de-diseño-de-software)
5. [Stack tecnológico](#5-stack-tecnológico)
6. [Endpoints REST](#6-endpoints-rest)
7. [Configuración](#7-configuración)

---

## 1. Descripción general

El **MS Portal Pacientes** es el microservicio que expone una interfaz de consulta de solo lectura para los pacientes del sistema RedNorte. Permite que un paciente consulte el estado de sus solicitudes médicas usando únicamente su RUT, sin necesidad de autenticación propia.

A diferencia de los demás microservicios, no gestiona datos propios de solicitudes — los obtiene directamente del MS Lista de Espera a través de llamadas HTTP via RestTemplate.

### Responsabilidades principales

- Exponer endpoints de solo lectura para consulta de solicitudes por RUT
- Consultar al MS Lista de Espera para obtener datos de solicitudes
- Retornar vista resumida o detallada según el endpoint consultado

### Lo que NO hace este microservicio

- No gestiona identidades de usuario ni requiere autenticación
- No escribe solicitudes ni modifica estados
- No accede directamente a la BD de otros microservicios
- No publica ni consume eventos RabbitMQ
- No tiene base de datos propia

---

## 2. Arquetipo del microservicio

### Estructura de carpetas

```
ms-portal-pacientes/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── cl/rednorte/ms_portal_pacientes/
│   │   │       ├── controller/              # Endpoints REST de solo lectura
│   │   │       ├── service/                 # Interfaz de servicio
│   │   │       │   └── impl/               # Implementación de servicio
│   │   │       ├── dto/
│   │   │       │   └── response/           # DTOs de salida
│   │   │       │       ├── SolicitudResumenResponse.java
│   │   │       │       ├── SolicitudDetalleResponse.java
│   │   │       │       ├── HistorialEstadoResponse.java
│   │   │       │       └── PageResponse.java
│   │   │       ├── config/
│   │   │       │   └── RestTemplateConfig.java  # Cliente HTTP
│   │   │       └── MsPortalPacientesApplication.java
│   │   └── resources/
│   │       ├── application.yaml            # Configuración principal
│   │       └── application-dev.yaml       # Configuración local
│   └── test/
│       └── java/cl/rednorte/ms_portal_pacientes/
├── .gitignore
├── pom.xml
└── README.md
```

### Diferencia clave con otros microservicios

| Característica | MS Lista de Espera | MS Portal Pacientes |
|---------------|-------------------|---------------------|
| Base de datos | ✅ MySQL propia | ❌ Sin BD |
| Autenticación | ✅ JWT requerido | ❌ Solo RUT |
| Escritura | ✅ POST, PATCH | ❌ Solo GET |
| RabbitMQ | ✅ Publica eventos | ❌ No aplica |
| Fuente de datos | BD propia | MS Lista de Espera via HTTP |

### Convención de nombres

| Tipo | Convención | Ejemplo |
|------|-----------|---------|
| Servicios (interfaz) | Entidad + Service | `PortalPacientesService` |
| Servicios (impl) | Entidad + ServiceImpl | `PortalPacientesServiceImpl` |
| Controladores | Entidad + Controller | `PortalPacientesController` |
| DTOs Response | Entidad + Response | `SolicitudResumenResponse` |
| Configuraciones | Tecnología + Config | `RestTemplateConfig` |

---

## 3. Arquitectura

### Posición en la arquitectura global

```
Paciente (RUT)
      │
      │ HTTP sin autenticación
      ▼
BFF Gateway (puerto 8090)
      │ /bff/portal-pacientes/** → permitAll()
      ▼
MS Portal Pacientes (puerto 8088)
      │ RestTemplate
      ▼
MS Lista de Espera (puerto 8085)
      │
      ▼
MySQL — db_lista_espera
```

### Flujo de una consulta de paciente

```
1. Paciente ingresa RUT en el portal web
2. Frontend envía GET /bff/portal-pacientes/solicitudes?rutPaciente={rut}
3. BFF Gateway enruta a MS Portal Pacientes sin validar JWT
4. MS Portal Pacientes llama al MS Lista de Espera via RestTemplate
5. MS Lista de Espera retorna las solicitudes del paciente
6. MS Portal Pacientes retorna la respuesta al paciente
```

### Comunicación con otros microservicios

| Dirección | Destino | Mecanismo | Propósito |
|-----------|---------|-----------|-----------|
| Entrada | BFF Gateway | REST/HTTP | Recibe consultas del paciente |
| Salida | MS Lista de Espera | RestTemplate HTTP | Obtiene datos de solicitudes |

---

## 4. Patrones de diseño de software

### 4.1. Service Layer Pattern

**¿Qué es?**
Separa la lógica de negocio en una capa dedicada entre el Controller y la comunicación externa.

**¿Dónde se aplica?**
En `PortalPacientesService.java` y `PortalPacientesServiceImpl.java`:

```java
// Interfaz — define el contrato
public interface PortalPacientesService {
    PageResponse<SolicitudResumenResponse> obtenerSolicitudes(
        String rutPaciente, int page, int size);
    SolicitudDetalleResponse obtenerDetalle(Long id, String rutPaciente);
}
```

**¿Por qué se usa?**
- El Controller no sabe cómo se obtienen los datos — solo llama al Service
- Si en el futuro se cambia RestTemplate por Feign, solo se cambia la implementación
- Facilita las pruebas unitarias — se puede mockear el Service

---

### 4.2. DTO Pattern

**¿Qué es?**
Objetos que representan exactamente los datos que retorna el sistema al paciente.

**¿Dónde se aplica?**
En la carpeta `dto/response/`:

| DTO | Propósito |
|-----|-----------|
| `SolicitudResumenResponse` | Vista resumida para lista de solicitudes |
| `SolicitudDetalleResponse` | Vista completa con historial de estados |
| `HistorialEstadoResponse` | Registro de un cambio de estado |
| `PageResponse<T>` | Wrapper genérico para listas paginadas |

**¿Por qué se usa?**
- Controla exactamente qué información ve el paciente
- El paciente nunca ve datos internos como `rutFuncionario` o campos de auditoría
- `PageResponse<T>` es genérico y reutilizable para cualquier tipo de lista

---

### 4.3. RestTemplate Pattern

**¿Qué es?**
Cliente HTTP de Spring para llamar a otros servicios de forma sincrónica.

**¿Dónde se aplica?**
En `RestTemplateConfig.java` y `PortalPacientesServiceImpl.java`:

```java
// Configuración como Bean
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

// Uso en el Service
PageResponse<SolicitudResumenResponse> response = restTemplate.exchange(
    url,
    HttpMethod.GET,
    null,
    new ParameterizedTypeReference<PageResponse<SolicitudResumenResponse>>() {}
).getBody();
```

**¿Por qué se usa?**
- El MS Portal Pacientes no tiene BD propia — los datos vienen del MS Lista de Espera
- RestTemplate permite hacer llamadas HTTP de forma simple y declarativa
- `ParameterizedTypeReference` permite deserializar respuestas genéricas como `PageResponse<T>`

---

### 4.4. Manejo de errores con ResponseStatusException

**¿Qué es?**
Maneja los errores de comunicación con el MS Lista de Espera y los traduce en respuestas HTTP apropiadas para el paciente.

**¿Dónde se aplica?**
En `PortalPacientesServiceImpl.java`:

```java
try {
    // llamada al MS Lista de Espera
} catch (Exception e) {
    log.error("Error consultando MS Lista de Espera: {}", e.getMessage());
    throw new ResponseStatusException(
        HttpStatus.SERVICE_UNAVAILABLE,
        "Servicio no disponible temporalmente");
}
```

**¿Por qué se usa?**
- Si el MS Lista de Espera está caído, el paciente recibe un 503 claro en lugar de un error genérico
- Evita exponer detalles internos del sistema al paciente
- El log registra el error real para que los desarrolladores puedan diagnosticarlo

---

## 5. Stack tecnológico

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| Java | 21 LTS | Lenguaje de programación |
| Spring Boot | 3.5.14 | Framework principal |
| Spring Web | 3.5.14 | Endpoints REST y RestTemplate |
| Lombok | latest | Reducción de código boilerplate |
| Maven | 3.9.15 | Gestión de dependencias y build |

---

## 6. Endpoints REST

### Base URL
```
http://localhost:8088
```

### GET /portal/solicitudes
Retorna lista paginada de solicitudes del paciente en vista resumida.

**Query params:**

| Parámetro | Tipo | Requerido | Descripción |
|-----------|------|-----------|-------------|
| rutPaciente | String | Sí | RUT del paciente |
| page | int | No (default 0) | Número de página |
| size | int | No (default 20) | Tamaño de página |

**Request:**
```
GET /portal/solicitudes?rutPaciente=12345678-9
```

**Response 200:**
```json
{
  "content": [
    {
      "id": 1,
      "rutPaciente": "12345678-9",
      "especialidad": "Cardiología",
      "prioridad": 1,
      "estado": "EN_ESPERA",
      "fechaRegistro": "2026-05-13T10:00:00"
    }
  ],
  "totalElements": 2,
  "totalPages": 1,
  "currentPage": 0
}
```

**Response 503:** MS Lista de Espera no disponible

---

### GET /portal/solicitudes/{id}
Retorna detalle completo de una solicitud con historial de estados.

**Path param:** id → Long

**Query params:**

| Parámetro | Tipo | Requerido | Descripción |
|-----------|------|-----------|-------------|
| rutPaciente | String | Sí | RUT del paciente para validación |

**Request:**
```
GET /portal/solicitudes/1?rutPaciente=12345678-9
```

**Response 200:**
```json
{
  "id": 1,
  "rutPaciente": "12345678-9",
  "especialidad": "Cardiología",
  "diagnostico": "Dolor torácico crónico",
  "esGES": true,
  "patologiaGES": "Infarto agudo al miocardio",
  "nivelUrgencia": "GES",
  "esVulnerable": true,
  "tipoVulnerabilidad": "Adulto mayor",
  "prioridad": 1,
  "estado": "EN_ESPERA",
  "fechaRegistro": "2026-05-13T10:00:00",
  "fechaActualizacion": "2026-05-13T10:00:00",
  "historial": [
    {
      "estadoAnterior": null,
      "estadoNuevo": "EN_ESPERA",
      "motivo": null,
      "fechaCambio": "2026-05-13T10:00:00"
    }
  ]
}
```

**Response 404:** Solicitud no encontrada
**Response 503:** MS Lista de Espera no disponible

---

## 7. Configuración

### application.yaml

```yaml
spring:
  application:
    name: ms-portal-pacientes

server:
  port: 8088

ms:
  lista-espera:
    base-url: ${MS_LISTA_ESPERA_URL:http://localhost:8085}
```

### Variables de entorno

| Variable | Descripción | Default |
|----------|-------------|---------|
| MS_LISTA_ESPERA_URL | URL del MS Lista de Espera | http://localhost:8085 |

### Cómo ejecutar localmente

```bash
# 1. Clonar el repositorio
git clone https://github.com/Fullstack3RedNorte/MS-PortalPacientes.git

# 2. Asegurarse que el MS Lista de Espera esté corriendo en puerto 8085

# 3. Ejecutar el proyecto
cd MS-PortalPacientes
mvn spring-boot:run
```

### Verificar endpoints

```bash
# Listar solicitudes del paciente
GET http://localhost:8088/portal/solicitudes?rutPaciente=12345678-9

# Ver detalle de una solicitud
GET http://localhost:8088/portal/solicitudes/1?rutPaciente=12345678-9
```

### Dependencias del sistema

```
MS Lista de Espera (8085) → debe estar corriendo para que funcionen los endpoints
XAMPP/MySQL              → NO requerido, este MS no tiene BD propia
RabbitMQ                 → NO requerido, este MS no usa mensajería
```

---

*Documentación generada para el proyecto semestral Fullstack III — RedNorte — DuocUC 2026*
