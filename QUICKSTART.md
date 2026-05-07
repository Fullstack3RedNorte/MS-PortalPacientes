# Ejecutar MS-PortalPacientes desde Docker Hub

## Opción 1: Con Docker Compose (Recomendado)

```powershell
# Clonar o descargar el proyecto
git clone <url-del-repo>
cd MS-PortalPacientes

# Iniciar todos los servicios
docker-compose up -d

# Verificar que están corriendo
docker-compose ps
```

**Acceso:**
- Aplicación: http://localhost:8081
- MySQL: localhost:3306

---

## Opción 2: Solo la aplicación (manual)

```powershell
# Descargar la imagen
docker pull sh1r8/ms-portal-pacientes:latest

# Ejecutar aplicación (con MySQL existente o separado)
docker run -d \
  -p 8081:8081 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/mydatabase \
  -e SPRING_DATASOURCE_USERNAME=myuser \
  -e SPRING_DATASOURCE_PASSWORD=secret \
  --name portal-app \
  sh1r8/ms-portal-pacientes:latest
```

---

## Parar servicios

```powershell
docker-compose down
```

## Limpiar completamente

```powershell
docker-compose down -v
```

---

## Información de la imagen

**Imagen:** `sh1r8/ms-portal-pacientes:latest`
**Base:** Eclipse Temurin 17 (JRE)
**Tamaño:** ~400MB (aprox)
**Puertos expuestos:** 8081

## Credenciales predeterminadas

```
Database: mydatabase
Usuario:  myuser
Contraseña: secret
```
