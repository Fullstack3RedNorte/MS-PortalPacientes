@echo off
REM Script para gestionar Docker Compose del proyecto MS-PortalPacientes

if "%1"=="" (
    echo Uso: docker-manage.bat [comando]
    echo.
    echo Comandos disponibles:
    echo   up       - Inicia los servicios (MySQL y aplicacion)
    echo   down     - Detiene los servicios
    echo   build    - Construye las imagenes Docker
    echo   logs     - Muestra los logs en tiempo real
    echo   clean    - Detiene y elimina volumenes
    echo.
    exit /b 1
)

if "%1"=="up" (
    echo Iniciando servicios...
    docker-compose up -d
    echo.
    echo Servicios iniciados:
    echo   - MySQL:         localhost:3306
    echo   - Aplicacion:    http://localhost:8081
    exit /b 0
)

if "%1"=="down" (
    echo Deteniendo servicios...
    docker-compose down
    exit /b 0
)

if "%1"=="build" (
    echo Construyendo imagenes...
    docker-compose build --no-cache
    exit /b 0
)

if "%1"=="logs" (
    echo Mostrando logs...
    docker-compose logs -f
    exit /b 0
)

if "%1"=="clean" (
    echo Limpiando recursos...
    docker-compose down -v
    echo Servicios y volumenes eliminados
    exit /b 0
)

echo Comando desconocido: %1
exit /b 1
