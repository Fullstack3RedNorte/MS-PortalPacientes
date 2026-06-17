# MS Portal Pacientes

Microservicio Portal de Pacientes para Red Norte.

## Tecnologías

- **Java 21**
- **Spring Boot 3.5.14**
- **Maven**
- **Lombok**

## Ejecutar Unit Tests

Para ejecutar las pruebas unitarias:

```bash
./mvnw clean test
```

## Reporte de Cobertura de Código (JaCoCo)

El proyecto incluye JaCoCo configurado para generar reportes de cobertura de código.

### Cobertura Actual

![JaCoCo Coverage Report](img/jacocotests.png)

**Métrica de Cobertura:**
- **Líneas cubiertas**: 95%
- **Clases cubiertas**: 100% 
- **Métodos totales**: 10 (1 no cubierto)

### Generar Reporte

El reporte se genera automáticamente al ejecutar:

```bash
./mvnw clean test
```

El reporte HTML interactivo se encuentra en: `target/site/jacoco/index.html`

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── cl/rednorte/ms_portal_pacientes/
│   │       ├── config/
│   │       ├── controller/
│   │       ├── dto/
│   │       └── service/
│   └── resources/
└── test/
    └── java/
```

## Pruebas Unitarias

Se incluyen pruebas para:
- **Controller**: Validación de endpoints
- **Service**: Lógica de negocio
- **Integration**: Tests de aplicación

Total de tests: **13**
