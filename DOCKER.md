# Docker Setup - MS-PortalPacientes

## Requisitos
- Docker Desktop instalado
- Docker Compose (incluido en Docker Desktop)

## Estructura de archivos Docker

- `Dockerfile` - Imagen multi-stage que compila y empaqueta la app
- `docker-compose.yaml` - Orquestación de MySQL y la aplicación
- `.dockerignore` - Archivos a excluir de la imagen
- `.env.example` - Variables de entorno (copiar a `.env` si se necesita)
- `docker-manage.bat` - Scripts helper para Windows

## Iniciar con Docker Compose

### Opción 1: Usar el script batch (Windows)
```powershell
.\docker-manage.bat up
```

### Opción 2: Comandos manuales
```powershell
# Iniciar servicios
docker-compose up -d

# Ver logs
docker-compose logs -f

# Detener servicios
docker-compose down
```

## Servicios levantados

- **MySQL**: `localhost:3306`
  - Database: `mydatabase`
  - Usuario: `myuser`
  - Contraseña: `secret`

- **Aplicación Spring Boot**: `http://localhost:8088`
  - POST `/api/consultas/{rut}` - Registrar consulta
  - GET `/api/consultas/{rut}/resumen` - Obtener resúmenes
  - GET `/api/solicitudes-resumen/{rut}` - Obtener resúmenes

## Comandos útiles

```powershell
# Construir imagenes sin caché
docker-compose build --no-cache

# Ver logs en tiempo real
docker-compose logs -f

# Ver logs específicos de la app
docker-compose logs -f portal-app

# Acceder a la terminal de MySQL
docker exec -it ms-portal-mysql mysql -u myuser -p mydatabase

# Limpiar todo (servicios + volúmenes)
docker-compose down -v
```

## Variables de ambiente

El archivo `.env` puede ser personalizado. Ejemplo (.env):
```
MYSQL_DATABASE=mydatabase
MYSQL_USER=myuser
MYSQL_PASSWORD=secret
```

## Troubleshooting

**Error: "Port 3306 is already in use"**
- Cambiar el puerto en `compose.yaml`: `- '3307:3306'`

**La app no conecta a MySQL**
- Verificar que MySQL esté healthy: `docker-compose ps`
- Ver logs: `docker-compose logs mysql`

**Reconstruir desde cero**
```powershell
docker-compose down -v
docker-compose build --no-cache
docker-compose up -d
```
